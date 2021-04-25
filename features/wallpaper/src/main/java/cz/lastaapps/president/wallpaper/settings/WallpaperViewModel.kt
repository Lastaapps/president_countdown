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

package cz.lastaapps.president.wallpaper.settings

import android.Manifest
import android.app.Application
import android.app.WallpaperManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.lastaapps.president.core.coroutines.collectAsync
import cz.lastaapps.president.core.coroutines.moveTo
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.wallpaper.WallpaperViewBitmapCreator
import cz.lastaapps.president.wallpaper.settings.files.ImageRepo
import cz.lastaapps.president.wallpaper.settings.options.ClockLayoutOptions
import cz.lastaapps.president.wallpaper.settings.options.ClockThemeOptions
import cz.lastaapps.president.wallpaper.settings.options.WallpaperOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.InputStream

private typealias Sett = SettingsRepo

/**
 * Maps UI with Settings layer
 * */
internal class WallpaperViewModel(private val app: Application) : AndroidViewModel(app) {

    companion object {
        private val TAG = WallpaperViewModel::class.simpleName
    }

    //settings relevant to settings, no to the actual wallpaper
    val isDayPreview =
        MutableStateFlow(app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_NO > 0)
    val isPortrait = MutableStateFlow(true)

    //if the device is physically in the portrait mode
    private val isDevicePortrait = MutableStateFlow(true)

    //output bitmap size
    private val canvasSize = MutableStateFlow(Rect(0, 0, 1, 1))

    fun setIsDay(isDay: Boolean) = isDayPreview.tryEmit(isDay)
    fun setIsPortrait(value: Boolean) = isPortrait.tryEmit(value)
    fun setIsDevicePortrait(value: Boolean) = isDevicePortrait.tryEmit(value)
    fun setCanvasSize(size: Rect) {
        if (!size.isEmpty)
            canvasSize.tryEmit(size)
        else
            Log.w(TAG, "Empty rectangle, ignoring")
    }

    //values fetched from settings based on the orientation
    val layoutOptions = MutableStateFlow(ClockLayoutOptions.default)

    init {
        viewModelScope.launch(Dispatchers.Default) {

            var job: Job? = null

            //fetches data from settings based on the current orientation
            isPortrait.collect { state ->

                job?.cancel()
                job = launch {
                    Sett.getClockLayoutOptions(state).moveTo(this, layoutOptions)
                }
            }
        }
    }

    fun setLayoutOptions(value: ClockLayoutOptions) = viewModelScope.launch {
        viewModelScope.launch {
            Sett.setClockLayoutOptions(value, isPortrait.value)
        }
    }


    /* ----- Others ----------------------------------------------------------------------------- */

    //isDark
    val uiMode = Sett.uiMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)
    val rotationFix = Sett.rotationFix
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun setUIMode(uiMode: Int) {
        viewModelScope.launch { Sett.setUIMode(uiMode) }
    }

    fun setRotationFix(state: Boolean) {
        viewModelScope.launch { Sett.setRotationFix(state) }
    }


    /* ----- Colors ----------------------------------------------------------------------------- */

    val themeOptions = MutableStateFlow(ClockThemeOptions.defaultLight)

    init {
        viewModelScope.launch(Dispatchers.Default) {

            var job: Job? = null

            //fetches data from settings based on the current orientation
            isDayPreview.combine(isPortrait) { v1, v2 -> v1 to v2 }.collect { state ->

                job?.cancel()
                job = launch {
                    Sett.getClockThemeOptions(state.first, state.second).moveTo(this, themeOptions)
                }
            }
        }
    }

    fun setThemeOptions(value: ClockThemeOptions) {
        viewModelScope.launch {
            Sett.setClockThemeOptions(value, isDayPreview.value, isPortrait.value)
        }
    }


    /* ----- Wallpaper -------------------------------------------------------------------------- */
    val wallpaperOptions = MutableStateFlow(WallpaperOptions.default)

    init {
        viewModelScope.launch(Dispatchers.Default) {

            var job: Job? = null

            //fetches data from settings based on the current orientation
            isPortrait.collect { state ->

                job?.cancel()
                job = launch {
                    if (state) {
                        Sett.wallpaperPortrait.moveTo(this, wallpaperOptions)
                    } else {
                        Sett.wallpaperLandscape.moveTo(this, wallpaperOptions)
                    }
                }
            }
        }
    }

    fun setWallpaperOptions(value: WallpaperOptions) {

        var correctedValue = value

        if (correctedValue.rotate > 3)
            correctedValue = correctedValue.copy(rotate = 0)
        if (correctedValue.rotate < 0)
            correctedValue = correctedValue.copy(rotate = 3)

        if (isPortrait.value)
            Sett.setWallpaperPortrait(correctedValue)
        else
            Sett.setWallpaperLandscape(correctedValue)
    }

    /**
     * Handles when an user has selected an image
     * */
    fun handleUri(uri: Uri) {
        viewModelScope.launch {
            try {
                val input: InputStream = app.contentResolver!!.openInputStream(uri)!!
                val bitmap = BitmapFactory.decodeStream(input)
                handleBitmap(bitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * load system background
     * */
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun handleSystemBackground() {
        viewModelScope.launch {
            try {
                val bitmap = WallpaperManager.getInstance(app).drawable?.toBitmap()!!
                handleBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * New bitmap requested as background
     * */
    private fun handleBitmap(bitmap: Bitmap) {
        ImageRepo.setBitmaps(app, bitmap, isPortrait.value)
    }

    /**
     * Copy settings and wallpaper bitmap from the other mode
     * */
    fun copyWallpaperFromTheOtherMode() {
        ImageRepo.copyToOther(app, !isPortrait.value)

        if (!isPortrait.value)
            Sett.setWallpaperLandscape(wallpaperOptions.value)
        else
            Sett.setWallpaperPortrait(wallpaperOptions.value)
    }


    /* ----- Generating bitmaps ----------------------------------------------------------------- */

    /**
     * generates new bitmap every second
     * */
    fun subscribeForBitmap(scope: CoroutineScope): StateFlow<ImageBitmap> {

        //creates bitmaps and stores settings
        val bitmapCreator = WallpaperViewBitmapCreator()

        //triggered when any config changes
        val configUpdated = MutableStateFlow(Any())

        val collector: suspend (value: Any?) -> Unit = {
            configUpdated.emit(Any())
        }

        //observes for the changes
        Sett.listAllVariables().toMutableList().also {
            it.addAll(ImageRepo.listAllVariables(app))
            it.addAll(listOf(isDayPreview, isPortrait, isDevicePortrait, canvasSize))
        }.forEach {
            it.collectAsync(scope, collector)
        }

        val output = MutableStateFlow(ImageBitmap(1, 1))
        val states = CurrentState.getCurrentBuffered(scope)

        //updates configs when changed
        scope.launch {
            configUpdated.collectLatest {

                onConfigChanged(bitmapCreator)

                states.replayCache.getOrNull(0)?.let {
                    output.emit(createBitmap(bitmapCreator, it))
                }
            }
        }

        //updates with the current time
        scope.launch {
            states.collect {
                output.emit(createBitmap(bitmapCreator, it))
            }
        }

        return output
    }

    private suspend fun createBitmap(
        bitmapCreator: WallpaperViewBitmapCreator,
        state: CurrentState
    ): ImageBitmap {

        if (!bitmapCreator.isInitialized())
            onConfigChanged(bitmapCreator)

        return bitmapCreator.createBitmap(state).asImageBitmap()
    }

    /**
     * Called when something changes
     * */
    private suspend fun onConfigChanged(bitmapCreator: WallpaperViewBitmapCreator) {

        val showPortrait =
            if (!isDevicePortrait.value) isPortrait.value else !isPortrait.value

        val wallpaper = if (isPortrait.value)
            if (wallpaperOptions.value.enabled)
                ImageRepo.getBitmaps(app, true).value
            else
                null
        else
            if (wallpaperOptions.value.enabled)
                ImageRepo.getBitmaps(app, false).value
            else
                null

        bitmapCreator.updateConfig(
            app,
            rect = canvasSize.value.let {
                if (showPortrait) it
                else Rect(0, 0, it.height(), it.width())
            },
            rotationFix = true,
            rotation = if (showPortrait) 0 else 3,
            uiMode = if (isDayPreview.value) Configuration.UI_MODE_NIGHT_NO else Configuration.UI_MODE_NIGHT_YES,
            layoutOptions = layoutOptions.value,
            themeOptions = themeOptions.value,
            wallpaperBitmap = wallpaper,
            wallpaperOptions = wallpaperOptions.value,
        )
    }
}
