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

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import cz.lastaapps.president.core.App
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Stores settings for the Wallpaper
 * */
internal object SettingsRepo {

    private val TAG = SettingsRepo::class.simpleName

    private const val SP_NAME = "sp_wallpaper_settings"

    private const val biasVertPortKey = "biasVertPort"
    private const val biasHorzPortKey = "biasHorzPort"
    private const val biasVertLandKey = "biasVertLand"
    private const val biasHorzLandKey = "biasHorzLand"
    private const val scalePortKey = "scalePort"
    private const val scaleLandKey = "scaleLand"
    private const val uiModeKey = "uiMode"
    private const val rotationFixKey = "rotationFix"
    private const val foregroundLightKey = "foregroundLight"
    private const val foregroundDarkKey = "foregroundDark"
    private const val backgroundLightKey = "backgroundLight"
    private const val backgroundDarkKey = "backgroundDark"

    init {
        Log.i(TAG, "Initializing")
    }

    private val sp = App.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    val biasVertPort = getFloat(biasVertPortKey, .5f)
    val biasHorzPort = getFloat(biasHorzPortKey, .5f)
    val biasVertLand = getFloat(biasVertLandKey, .5f)
    val biasHorzLand = getFloat(biasHorzLandKey, .5f)
    val scalePort = getFloat(scalePortKey, 1f)
    val scaleLand = getFloat(scaleLandKey, 1f)
    val uiMode = getInt(uiModeKey, Configuration.UI_MODE_NIGHT_UNDEFINED)
    val rotationFix = getBoolean(rotationFixKey, false)
    val foregroundLight = getColor(foregroundLightKey, Color(0xff000000))
    val foregroundDark = getColor(foregroundDarkKey, Color(0xffffffff))
    val backgroundLight = getColor(backgroundLightKey, Color(0xffffffff))
    val backgroundDark = getColor(backgroundDarkKey, Color(0xff333333))

    fun setBiasVertPort(value: Float) {
        biasVertPort.tryEmit(value); setFloat(biasVertPortKey, value)
    }

    fun setBiasHorzPort(value: Float) {
        biasHorzPort.tryEmit(value); setFloat(biasHorzPortKey, value)
    }

    fun setBiasVertLand(value: Float) {
        biasVertLand.tryEmit(value); setFloat(biasVertLandKey, value)
    }

    fun setBiasHorzLand(value: Float) {
        biasHorzLand.tryEmit(value); setFloat(biasHorzLandKey, value)
    }

    fun setScalePort(value: Float) {
        scalePort.tryEmit(value); setFloat(scalePortKey, value)
    }

    fun setScaleLand(value: Float) {
        scaleLand.tryEmit(value); setFloat(scaleLandKey, value)
    }

    fun setUIMode(value: Int) {
        uiMode.tryEmit(value); setInt(uiModeKey, value)
    }

    fun setRotationFix(value: Boolean) {
        rotationFix.tryEmit(value); setBoolean(rotationFixKey, value)
    }

    fun setForegroundLight(value: Color) {
        foregroundLight.tryEmit(value); setColor(foregroundLightKey, value)
    }

    fun setForegroundDark(value: Color) {
        foregroundDark.tryEmit(value); setColor(foregroundDarkKey, value)
    }

    fun setBackgroundLight(value: Color) {
        backgroundLight.tryEmit(value); setColor(backgroundLightKey, value)
    }

    fun setBackgroundDark(value: Color) {
        backgroundDark.tryEmit(value); setColor(backgroundDarkKey, value)
    }


    private fun getFloat(key: String, default: Float) =
        MutableStateFlow(sp.getFloat(key, default))

    private fun getInt(key: String, default: Int) =
        MutableStateFlow(sp.getInt(key, default))

    private fun getBoolean(key: String, default: Boolean) =
        MutableStateFlow(sp.getBoolean(key, default))

    private fun getColor(key: String, default: Color) =
        MutableStateFlow(Color(sp.getInt(key, default.toArgb())))

    private fun setFloat(key: String, value: Float) = sp.edit().putFloat(key, value).apply()
    private fun setInt(key: String, value: Int) = sp.edit().putInt(key, value).apply()
    private fun setBoolean(key: String, value: Boolean) = sp.edit().putBoolean(key, value).apply()
    private fun setColor(key: String, value: Color) = sp.edit().putInt(key, value.toArgb()).apply()

}

internal fun SettingsRepo.getScale(isPortrait: Boolean) = if (isPortrait) scalePort else scaleLand

internal fun SettingsRepo.getVerticalBias(isPortrait: Boolean) =
    if (isPortrait) biasVertPort else biasVertLand

internal fun SettingsRepo.getHorizontalBias(isPortrait: Boolean) =
    if (isPortrait) biasHorzPort else biasHorzLand

internal fun SettingsRepo.getForegroundColor(mode: Int) =
    if (mode and Configuration.UI_MODE_NIGHT_YES == 0)
        foregroundLight
    else
        foregroundDark

internal fun SettingsRepo.getBackgroundColor(mode: Int) =
    if (mode and Configuration.UI_MODE_NIGHT_YES == 0)
        backgroundLight
    else
        backgroundDark

