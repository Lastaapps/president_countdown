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

package cz.lastaapps.president.wallpaper.service

/**
 * Compose version of view creation
 * Compose doesn't support that (or I haven't found out how)
 * */
/*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.service.wallpaper.WallpaperService
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import cz.lastaapps.president.clock.CreateClock
import cz.lastaapps.ui.common.themes.MainTheme
import kotlinx.coroutines.*

internal class WallpaperServiceCompose : WallpaperService() {

    private val TAG = WallpaperService::class.simpleName

    override fun onCreateEngine(): Engine {
        return BecherovkaEngine()
    }

    inner class BecherovkaEngine : Engine() {
        private lateinit var holder: SurfaceHolder
        private var visible = false
        private lateinit var view: AbstractComposeView
        private val rect: Rect = Rect()

        private var scope: CoroutineScope? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            holder = surfaceHolder
            loadData()

            scope?.cancel()
            scope = CoroutineScope(Dispatchers.Main).also {
                it.launch {
                    draw()
                }
            }
        }

        override fun onDesiredSizeChanged(desiredWidth: Int, desiredHeight: Int) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight)
            rect[0, 0, desiredMinimumWidth] = desiredMinimumHeight
        }

        @SuppressLint("NewApi")
        private fun loadData() {
            /*val displayMetrics = DisplayMetrics()
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager // the results will be higher than using the activity context object or the getWindowManager() shortcut

            when (Build.VERSION.SDK_INT) {
                in 1..17 -> wm.defaultDisplay.getMetrics(displayMetrics)
                in 18..29 -> wm.defaultDisplay.getRealMetrics(displayMetrics)
                in 30..Int.MAX_VALUE -> display?.getRealMetrics(displayMetrics)
                else -> throw IllegalStateException("Wtf has just happened?!")
            }

            val scrSize = Point()
            scrSize.x = displayMetrics.widthPixels
            scrSize.y = displayMetrics.heightPixels
            rect[0, 0, scrSize.x] = scrSize.y*/

            rect[0, 0, desiredMinimumWidth] = desiredMinimumHeight

            view = MyComposeView(displayContext!!).apply {
                setParentCompositionReference(Recomposer.current())
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MainTheme {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CreateClock()
                        }
                    }
                }
                //createComposition()
            }
        }

        private suspend fun draw() {

//rect.set(padding, padding, padding, padding);
//Measure the view at the exact dimensions (otherwise the text won't center correctly)
            val widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY)
            val heightSpec =
                View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY)
            view.measure(widthSpec, heightSpec)

            view.layout(0, 0, rect.width(), rect.height())

            view.createComposition()


            while (true) {
                if (visible) {
                    Log.v(TAG, "drawing")

                    //Translate the Canvas into position and draw it
                    val canvas = holder.lockCanvas()
                    canvas.save()
                    canvas.translate(rect.left.toFloat(), rect.top.toFloat())

                    //Lay the view out at the rect width and height
                    view.draw(canvas)
                    canvas.restore()
                    holder.unlockCanvasAndPost(canvas)
                }

                delay(1000 + 100 - System.currentTimeMillis() % 1000)
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            this.visible = visible
        }

        override fun onDestroy() {
            super.onDestroy()
            scope?.cancel()
        }
    }
}

class MyComposeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private val content = mutableStateOf<(@Composable () -> Unit)?>(null)

    @Suppress("RedundantVisibilityModifier")
    protected override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    @Composable
    override fun Content() {
        content.value?.invoke()
    }

    /**
     * Set the Jetpack Compose UI content for this view.
     * Initial composition will occur when the view becomes attached to a window or when
     * [createComposition] is called, whichever comes first.
     */
    fun setContent(content: @Composable () -> Unit) {
        shouldCreateCompositionOnAttachedToWindow = false
        this.content.value = content
        if (isAttachedToWindow) {
            createComposition()
        }
    }
}*/
