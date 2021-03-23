/*
 *   Copyright 2021, Petr Laštovička as Lasta apps, All rights reserved
 *
 *     This file is part of President Countdown.
 *
 *     This app is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This app is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this app.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package cz.lastaapps.president.wallpaper

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.text.HtmlCompat
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.core.president.TimePlurals
import cz.lastaapps.president.core.president.get
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Creates bitmaps of the clock view based on options inputted
 * */
internal class WallpaperViewBitmapCreator {

    companion object {
        private val TAG = WallpaperViewBitmapCreator::class.simpleName
    }

    private var initialized = false
    private var mRect: Rect = Rect()
    private var mRotationFix: Boolean = false
    private var mRotation: Float = 0f

    private lateinit var mView: View

    //makes everything synchronized
    private val mutex = Mutex()

    fun isInitialized() = initialized

    /**
     * Set the view options
     * Had to be called before #createBitmap()
     * */
    suspend fun updateConfig(
        context: Context,
        rect: Rect,
        scale: Float,
        rotationFix: Boolean,
        verticalBias: Float,
        horizontalBias: Float,
        rotation: Int,
        uiMode: Int,
        foregroundColor: Color,
        backgroundColor: Color,
    ) = mutex.withLock {

        //input validation
        if (!initialized)
            Log.i(TAG, "Initializing the configurations")

        if (rect.isEmpty)
            throw IllegalArgumentException("Rect cannot be empty!")

        //theme selection - Day/Night
        val themeContext =
            ContextThemeWrapper(context, R.style.Theme_PresidentCountdown_NoActionBar).let {
                it.createConfigurationContext(
                    Resources.getSystem().configuration.also { config ->
                        config.uiMode = uiMode
                    }
                )
            }


        mView = (LayoutInflater.from(themeContext)
            .inflate(R.layout.wallpaper, null, false) as ConstraintLayout).apply {

            //clocks bias position
            val constraintSet = ConstraintSet()
            constraintSet.clone(this)
            constraintSet.setVerticalBias(R.id.clock_include, verticalBias)
            constraintSet.setHorizontalBias(R.id.clock_include, horizontalBias)
            constraintSet.applyTo(this)
        }
        //scales the view
        mView.findViewById<View>(R.id.clock_include).apply {
            scaleX = scale
            scaleY = scale
        }

        mRect = rect

        //rotates the view
        mRotation = (rotation % 4) * 90f * if (rotationFix) 1 else 0
        mRotationFix = rotationFix

        updateWallpaperColors(mView, foregroundColor, backgroundColor)

        initialized = true
    }

    /**
     * Creates bitmap for state given
     * */
    suspend fun createBitmap(state: CurrentState): Bitmap = mutex.withLock {

        if (!initialized) throw IllegalStateException("Not initialized, call updateConfig() first")

        val view = mView

        //applies the state to the view
        updateWallpaperView(view, state)

        //layout the view
        val vWidth: Int = mRect.width()
        val vHeight: Int = mRect.height()

        val widthSpec =
            View.MeasureSpec.makeMeasureSpec(vWidth, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(vHeight, View.MeasureSpec.EXACTLY)

        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, vWidth, vHeight)

        //creates bitmap
        return view.getViewsBitmap(vWidth, vHeight).rotate(mRotation)
    }
}

/**
 * @return Bitmap of the view at given size
 * */
private fun View.getViewsBitmap(width: Int, height: Int): Bitmap {
    val returnedBitmap =
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(returnedBitmap)
    draw(canvas)
    return returnedBitmap
}

/**
 * @return Bitmap rotated by degrees given
 * */
private fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

//ids of the components in the view
private val values = listOf(R.id.yv, R.id.dv, R.id.hv, R.id.mv, R.id.sv)
private val units = listOf(R.id.yu, R.id.du, R.id.hu, R.id.mu, R.id.su)

private fun updateWallpaperColors(view: View, foregroundColor: Color, backgroundColor: Color) {
    //ignores the first red digit
    values.filter { it != R.id.yv }.forEach {
        view.findViewById<TextView>(it).setTextColor(foregroundColor.toArgb())
    }
    units.forEach {
        view.findViewById<TextView>(it).setTextColor(foregroundColor.toArgb())
    }

    view.setBackgroundColor(backgroundColor.toArgb())
}

/**
 * Updates view given with state given
 *  */
private fun updateWallpaperView(view: View, state: CurrentState) {
    val timeRoot = view.findViewById<Flow>(R.id.time_root)
    val stateTV = view.findViewById<TextView>(R.id.state)

    if (state.state.isTimeRemainingSupported) {

        stateTV.visibility = View.GONE
        timeRoot.visibility = View.VISIBLE

        val plurals = TimePlurals(view.context)

        for (i in 0 until 5) {
            val value = state[i]

            view.findViewById<TextView>(values[i]).text = value.toString()
            view.findViewById<TextView>(units[i]).text = plurals.getByIndex(i, value.toInt())
        }
    } else {
        stateTV.visibility = View.VISIBLE
        timeRoot.visibility = View.GONE
        stateTV.text = HtmlCompat.fromHtml(
            view.context.getString(state.state.formattedStringId),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}