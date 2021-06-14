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

import android.app.WallpaperColors
import android.content.res.Configuration
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.service.wallpaper.WallpaperService
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.Surface
import android.widget.Toast
import androidx.annotation.RequiresApi
import cz.lastaapps.president.core.coroutines.collectAsync
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.wallpaper.R
import cz.lastaapps.president.wallpaper.WallpaperViewBitmapCreator
import cz.lastaapps.president.wallpaper.settings.*
import cz.lastaapps.president.wallpaper.settings.files.ImageRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

//it look better
private typealias Sett = SettingsRepo

/**
 * Service containing wallpaper engine
 * */
class PresidentWallpaperService : WallpaperService() {

    companion object {
        private val TAG = PresidentWallpaperService::class.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Creating service")
    }

    //stores engine reference to notify it about config changes
    private var engine: BecherovkaEngine? = null
    override fun onCreateEngine(): Engine {

        Log.i(TAG, "Registering engine")
        return BecherovkaEngine().also {
            engine = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(TAG, "Destroying")
    }

    /**
     * Notifies the engine about config changes
     * */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Log.i(TAG, "Config changed")
        engine?.displayChanged()
    }

    /**
     * Becherovka == the favourite drink of Miloš Zeman
     *
     * */
    inner class BecherovkaEngine : Engine() {

        @Suppress("PrivatePropertyName")
        private val TAG = BecherovkaEngine::class.simpleName + '@' + hashCode()

        private val wm by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }
        private val bitmapGenerator = WallpaperViewBitmapCreator()

        private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        private var job: Job? = null

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            Log.i(TAG, "Creating engine")

            setTouchEventsEnabled(false)

            displayChanged()
            observeSettings()
        }

        /**
         * Watches settings for updates
         * */
        private fun observeSettings() {

            //reduces the first wave of vales flow from settings
            var enabled = false

            val collector: suspend ((Any?) -> Unit) = { _ -> if (enabled) displayChanged() }

            //observers for the changes in settings
            Sett.listAllVariables().toMutableList().also {
                it.addAll(ImageRepo.listAllVariables(this@PresidentWallpaperService))
            }.forEach {
                it.collectAsync(scope, collector)
            }

            scope.launch {
                enabled = true
                collector(Any())
            }
        }

        /**
         * Called when ever some display property changes
         * Updates options in the WallpaperViewBitmapCreator
         * */
        internal fun displayChanged() = scope.launch(Dispatchers.Default) {

            val config = this@PresidentWallpaperService.resources.configuration

            @Suppress("DEPRECATION")
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                displayContext?.display else wm.defaultDisplay
            display!!

            Log.d(TAG, "Config: ${config.orientation} ${config.uiMode} $config")
            Log.d(TAG, "Display orientation: ${display.rotation}")

            val rect = Rect().also {
                DisplayMetrics().apply {
                    display.getRealMetrics(this)
                    it.set(0, 0, widthPixels, heightPixels)
                    Log.d(TAG, "Display size: $it")
                }
            }

            val isPortrait =
                display.rotation in listOf(Surface.ROTATION_0, Surface.ROTATION_180)

            val userPreferredMode = Sett.uiMode.value
            val actualMode = when {
                (userPreferredMode and Configuration.UI_MODE_NIGHT_YES) > 0 -> Configuration.UI_MODE_NIGHT_YES
                (userPreferredMode and Configuration.UI_MODE_NIGHT_NO) > 0 -> Configuration.UI_MODE_NIGHT_NO
                else -> config.uiMode
            }

            val wallpaperOptions =
                if (isPortrait) Sett.wallpaperPortrait else Sett.wallpaperLandscape

            val wallpaper = if (isPortrait)
                if (wallpaperOptions.value.enabled)
                    ImageRepo.getBitmaps(this@PresidentWallpaperService, true).value
                else
                    null
            else
                if (wallpaperOptions.value.enabled)
                    ImageRepo.getBitmaps(this@PresidentWallpaperService, false).value
                else
                    null

            bitmapGenerator.updateConfig(
                this@PresidentWallpaperService,
                rect = rect,
                rotationFix = Sett.rotationFix.value,
                rotation = display.rotation,
                uiMode = actualMode,
                layoutOptions = Sett.getClockLayoutOptions(isPortrait).value,
                themeOptions = Sett.getClockThemeOptions(actualMode, isPortrait).value,
                wallpaperBitmap = wallpaper,
                wallpaperOptions = wallpaperOptions.value,
            )

            //updates colors
            launch {
                if (Build.VERSION.SDK_INT >= 27) {
                    delay(1000)
                    notifyColorsChanged()
                }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)

            Log.i(TAG, "Visibility changed to $visible")

            if (visible) {
                if (job == null) {
                    Log.d(TAG, "Starting drawing job")
                    job = draw()
                }
            } else {
                Log.d(TAG, "Canceling drawing job")
                job?.cancel()
                job = null
            }
        }

        //for wallpaper colors
        private var lastBitmap: Bitmap? = null

        /**
         * Draws the bitmap based on the current state
         * */
        private fun draw() = scope.launch(Dispatchers.Default) {
            Log.d(TAG, "Drawing")

            try {
                CurrentState.getCurrentBuffered(scope).collect { state ->

                    val bitmap = bitmapGenerator.createBitmap(state)

                    lastBitmap = bitmap

                    yield()

                    //ensures canvas doesn't change
                    withContext(Dispatchers.Main) {

                        //Translate the Canvas into position and draw it
                        val canvas = surfaceHolder.lockCanvas()
                        canvas.save()
                        canvas.translate(0f, 0f)

                        canvas.drawBitmap(bitmap, 0f, 0f, null)
                        canvas.restore()
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }

                    //If there is item skipped, it will be shown for a moment at least
                    delay(200)
                }
                Log.e(TAG, "Drawing ended unexpected!")
            } catch (e: Exception) {
                Log.e(TAG, "Drawing ended with an error: ${e.message}!")

                withContext(Dispatchers.Main) {
                    val message = getString(R.string.wallpaper_error, e.message)
                    Toast.makeText(this@PresidentWallpaperService, message, Toast.LENGTH_LONG)
                        .show()
                }

                e.printStackTrace()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O_MR1)
        override fun onComputeColors(): WallpaperColors? {
            val bitmap = lastBitmap
            return if (bitmap == null)
                super.onComputeColors()
            else
                WallpaperColors.fromBitmap(bitmap)
        }

        override fun onOffsetsChanged(
            xOffset: Float, yOffset: Float,
            xOffsetStep: Float, yOffsetStep: Float,
            xPixelOffset: Int, yPixelOffset: Int
        ) {
            super.onOffsetsChanged(
                xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset
            )
            Log.d(TAG, "Offset")
            displayChanged()
        }

        override fun onCommand(
            action: String?, x: Int, y: Int, z: Int,
            extras: Bundle?, resultRequested: Boolean
        ): Bundle? {
            //prevents random crashes
            if (action == null) {
                Log.e(TAG, "Returning null as response to a command")
                return null
            }
            return super.onCommand(action, x, y, z, extras, resultRequested)
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.i(TAG, "Destroying engine")

            scope.cancel()

            //removes the engine from cache
            if (engine == this)
                engine = null
        }
    }
}