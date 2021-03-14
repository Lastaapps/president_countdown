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

import android.app.Application
import android.content.res.Configuration
import android.graphics.Rect
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.lastaapps.president.core.coroutines.collectAsync
import cz.lastaapps.president.core.coroutines.moveTo
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.wallpaper.WallpaperViewBitmapCreator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private typealias Sett = SettingsRepo

/**
 * Maps UI with Settings layer
 * */
internal class WallpaperViewModel(private val app: Application) : AndroidViewModel(app) {

    companion object {
        private val TAG = WallpaperViewModel::class.simpleName
    }

    //settings relevant to settings, no to the actual wallpaper
    val orientationPortrait = MutableStateFlow(true)
    val isDayPreview =
        MutableStateFlow(app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_NO > 0)
    private val isLand = MutableStateFlow(false)
    private val canvasSize = MutableStateFlow(Rect(0, 0, 1, 1))

    fun setOrientation(isPortrait: Boolean) = orientationPortrait.tryEmit(isPortrait)
    fun setIsDay(isDay: Boolean) = isDayPreview.tryEmit(isDay)
    fun setIsLand(value: Boolean) = isLand.tryEmit(value)
    fun setCanvasSize(size: Rect) {
        if (!size.isEmpty)
            canvasSize.tryEmit(size)
        else
            Log.w(TAG, "Empty rectangle, ignoring")
    }

    //values fetched from settings based on the orientation
    val vertical = MutableStateFlow(.5f)
    val horizontal = MutableStateFlow(.5f)
    val scale = MutableStateFlow(1f)

    init {
        viewModelScope.launch(Dispatchers.Default) {

            var job: Job? = null

            //fetches data from settings based on the current orientation
            orientationPortrait.collect { state ->

                job?.cancel()
                job = launch {
                    if (state) {
                        //portrait
                        Sett.biasVertPort.moveTo(this, vertical)
                        Sett.biasHorzPort.moveTo(this, horizontal)
                        Sett.scalePort.moveTo(this, scale)
                    } else {
                        //landscape
                        Sett.biasVertLand.moveTo(this, vertical)
                        Sett.biasHorzLand.moveTo(this, horizontal)
                        Sett.scaleLand.moveTo(this, scale)
                    }
                }
            }
        }
    }

    /**
     * set scale for the current orientation
     * */
    fun setScale(value: Float) = viewModelScope.launch {
        if (orientationPortrait.value)
            Sett.setScalePort(value)
        else
            Sett.setScaleLand(value)
    }

    /**
     * set vertical bias for the current orientation
     * */
    fun setBiasVertical(value: Float) = viewModelScope.launch {
        if (orientationPortrait.value)
            Sett.setBiasVertPort(value)
        else
            Sett.setBiasVertLand(value)
    }

    /**
     * set horizontal bias for the current orientation
     * */
    fun setBiasHorizontal(value: Float) = viewModelScope.launch {
        if (orientationPortrait.value)
            Sett.setBiasHorzPort(value)
        else
            Sett.setBiasHorzLand(value)
    }

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

    val foregroundColor = MutableStateFlow(Color(0x00000000))
    val backgroundColor = MutableStateFlow(Color(0x00000000))

    init {
        viewModelScope.launch(Dispatchers.Default) {

            var job: Job? = null

            //fetches data from settings based on the current orientation
            isDayPreview.collect { state ->

                job?.cancel()
                job = launch {
                    if (state) {
                        //day theme
                        Sett.foregroundLight.moveTo(this, foregroundColor)
                        Sett.backgroundLight.moveTo(this, backgroundColor)
                    } else {
                        //night theme
                        Sett.foregroundDark.moveTo(this, foregroundColor)
                        Sett.backgroundDark.moveTo(this, backgroundColor)
                    }
                }
            }
        }
    }

    fun setForegroundColor(color: Color) {
        if (isDayPreview.value)
            Sett.setForegroundLight(color)
        else
            Sett.setForegroundDark(color)
    }

    fun setBackgroundColor(color: Color) {
        if (isDayPreview.value)
            Sett.setBackgroundLight(color)
        else
            Sett.setBackgroundDark(color)
    }

    /**
     * generates new bitmap every second
     * */
    fun subscribeForBitmap(scope: CoroutineScope): StateFlow<ImageBitmap> {

        val bitmapCreator = WallpaperViewBitmapCreator()

        var configUpdated = true

        val collector: suspend (value: Any) -> Unit = {
            configUpdated = true
        }

        //observes for the changes
        listOf(
            orientationPortrait,
            vertical, horizontal, scale,
            isDayPreview,
            foregroundColor, backgroundColor,
        ).forEach {
            it.collectAsync(scope, collector)
        }

        val output = MutableStateFlow(ImageBitmap(1, 1))
        val states = CurrentState.getCurrentBuffered(scope)

        scope.launch {
            while (true) {
                if (configUpdated) {
                    configUpdated = false

                    onConfigChanged(bitmapCreator)

                    states.replayCache.getOrNull(0)?.let {
                        output.emit(createBitmap(bitmapCreator, it))
                    }
                }
                delay(50)
            }
        }

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

    private suspend fun onConfigChanged(bitmapCreator: WallpaperViewBitmapCreator) {
        val orientation =
            if (isLand.value) orientationPortrait.value else !orientationPortrait.value

        bitmapCreator.updateConfig(
            app,
            rect = canvasSize.value.let {
                if (orientation) it
                else Rect(0, 0, it.height(), it.width())
            },
            scale = scale.value,
            rotationFix = true,
            verticalBias = vertical.value,
            horizontalBias = horizontal.value,
            rotation = if (orientation) 0 else 3,
            uiMode = if (isDayPreview.value) Configuration.UI_MODE_NIGHT_NO else Configuration.UI_MODE_NIGHT_YES,
            foregroundColor = foregroundColor.value,
            backgroundColor = backgroundColor.value,
        )
    }
}
