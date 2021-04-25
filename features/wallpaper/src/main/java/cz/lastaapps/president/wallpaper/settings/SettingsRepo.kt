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
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import cz.lastaapps.president.core.App
import cz.lastaapps.president.wallpaper.settings.options.ClockLayoutOptions
import cz.lastaapps.president.wallpaper.settings.options.ClockThemeOptions
import cz.lastaapps.president.wallpaper.settings.options.WallpaperOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Stores settings for the Wallpaper
 * */
internal object SettingsRepo {

    private val TAG = SettingsRepo::class.simpleName

    private const val SP_NAME = "sp_wallpaper_settings"

    //root keys
    private const val layoutKey = "layout"
    private const val themeKey = "theme"
    private const val wallpaperKey = "wallpaper"
    private const val rotationFixKey = "rotation-fix"
    private const val uiModeKey = "ui-mode"

    private const val portraitKey = "_portrait"
    private const val landscapeKey = "_landscape"

    private const val lightKey = "_light"
    private const val darkKey = "_dark"

    //individual properties
    private const val layoutVerticalBiasKey = "_vertical-bias"
    private const val layoutHorizontalBiasKey = "_horizontal-bias"
    private const val layoutScaleKey = "_scale"

    private const val themeForegroundKey = "_foreground"
    private const val themeBackgroundKey = "_background"
    private const val themeDifferYearKey = "_differ-year"
    private const val themeYearColorKey = "_year-color"

    private const val wallpaperEnabledKey = "_enabled"
    private const val wallpaperZoomKey = "_zoom"
    private const val wallpaperVertBiasKey = "_vert_bias"
    private const val wallpaperHortBiasKey = "_hort_bias"
    private const val wallpaperRotateKey = "_rotate"

    //complex keys
    private const val layoutPortraitKey = layoutKey + portraitKey
    private const val layoutLandscapeKey = layoutKey + landscapeKey

    private const val themeLightPortraitKey = themeKey + lightKey + portraitKey
    private const val themeDarkPortraitKey = themeKey + darkKey + portraitKey
    private const val themeLightLandscapeKey = themeKey + lightKey + landscapeKey
    private const val themeDarkLandscapeKey = themeKey + darkKey + landscapeKey

    private const val wallpaperPortraitKey = wallpaperKey + portraitKey
    private const val wallpaperLandscapeKey = wallpaperKey + landscapeKey


    init {
        Log.i(TAG, "Initializing")
    }

    private val sp = App.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    val uiMode = getInt(uiModeKey, Configuration.UI_MODE_NIGHT_UNDEFINED)
    val rotationFix = getBoolean(rotationFixKey, false)

    val layoutPortrait = getClockLayoutOptions(layoutPortraitKey, ClockLayoutOptions.default)
    val layoutLandscape = getClockLayoutOptions(layoutLandscapeKey, ClockLayoutOptions.default)

    val themeLightPortrait =
        getClockThemeOptions(themeLightPortraitKey, ClockThemeOptions.defaultLight)
    val themeDarkPortrait =
        getClockThemeOptions(themeDarkPortraitKey, ClockThemeOptions.defaultDark)
    val themeLightLandscape =
        getClockThemeOptions(themeLightLandscapeKey, ClockThemeOptions.defaultLight)
    val themeDarkLandscape =
        getClockThemeOptions(themeDarkLandscapeKey, ClockThemeOptions.defaultDark)

    val wallpaperPortrait = getWallpaperOptions(wallpaperPortraitKey, WallpaperOptions.default)
    val wallpaperLandscape = getWallpaperOptions(wallpaperLandscapeKey, WallpaperOptions.default)

    /**
     * Lists all the local flows, so they can be easily observed for change
     * */
    fun listAllVariables(): List<StateFlow<Any?>> = listOf(
        uiMode, rotationFix,
        layoutPortrait, layoutLandscape,
        themeLightPortrait, themeDarkPortrait,
        themeLightLandscape, themeDarkLandscape,
        wallpaperPortrait, wallpaperLandscape,
    )


    fun setLayoutPortrait(value: ClockLayoutOptions) {
        layoutPortrait.tryEmit(value)
        setClockLayout(layoutPortraitKey, value)
    }

    fun setLayoutLandscape(value: ClockLayoutOptions) {
        layoutLandscape.tryEmit(value)
        setClockLayout(layoutLandscapeKey, value)
    }

    fun setThemeLightPortrait(value: ClockThemeOptions) {
        themeLightPortrait.tryEmit(value)
        setClockTheme(themeLightPortraitKey, value)
    }

    fun setThemeDarkPortrait(value: ClockThemeOptions) {
        themeDarkPortrait.tryEmit(value)
        setClockTheme(themeDarkPortraitKey, value)
    }

    fun setThemeLightLandscape(value: ClockThemeOptions) {
        themeLightLandscape.tryEmit(value)
        setClockTheme(themeLightLandscapeKey, value)
    }

    fun setThemeDarkLandscape(value: ClockThemeOptions) {
        themeDarkLandscape.tryEmit(value)
        setClockTheme(themeDarkLandscapeKey, value)
    }

    fun setWallpaperPortrait(value: WallpaperOptions) {
        wallpaperPortrait.tryEmit(value); setWallpaperOptions(wallpaperPortraitKey, value)
    }

    fun setWallpaperLandscape(value: WallpaperOptions) {
        wallpaperLandscape.tryEmit(value); setWallpaperOptions(wallpaperLandscapeKey, value)
    }

    fun setUIMode(value: Int) {
        uiMode.tryEmit(value); setInt(uiModeKey, value)
    }

    fun setRotationFix(value: Boolean) {
        rotationFix.tryEmit(value); setBoolean(rotationFixKey, value)
    }


    private fun getInt(key: String, default: Int) =
        MutableStateFlow(sp.getInt(key, default))

    private fun getBoolean(key: String, default: Boolean) =
        MutableStateFlow(sp.getBoolean(key, default))

    private fun setInt(key: String, value: Int) = sp.edit().putInt(key, value).apply()
    private fun setBoolean(key: String, value: Boolean) = sp.edit().putBoolean(key, value).apply()

    /**
     * Load layout options for the key given
     * */
    private fun getClockLayoutOptions(
        key: String,
        default: ClockLayoutOptions
    ): MutableStateFlow<ClockLayoutOptions> {
        return MutableStateFlow(
            with(sp) {
                ClockLayoutOptions(
                    verticalBias = getFloat(key + layoutVerticalBiasKey, default.verticalBias),
                    horizontalBias = getFloat(
                        key + layoutHorizontalBiasKey,
                        default.horizontalBias
                    ),
                    scale = getFloat(key + layoutScaleKey, default.scale),
                )
            }
        )
    }

    /**
     * Saved layout data
     * */
    private fun setClockLayout(key: String, value: ClockLayoutOptions) {
        with(sp.edit()) {
            putFloat(key + layoutVerticalBiasKey, value.verticalBias)
            putFloat(key + layoutHorizontalBiasKey, value.horizontalBias)
            putFloat(key + layoutScaleKey, value.scale)
            apply()
        }
    }


    /**
     * Load theme configs
     * */
    private fun getClockThemeOptions(
        key: String,
        default: ClockThemeOptions
    ): MutableStateFlow<ClockThemeOptions> {
        return MutableStateFlow(
            with(sp) {
                ClockThemeOptions(
                    foreground = getColor(key + themeForegroundKey, default.foreground),
                    background = getColor(key + themeBackgroundKey, default.background),
                    differYear = getBoolean(key + themeDifferYearKey, default.differYear),
                    yearColor = getColor(key + themeYearColorKey, default.yearColor),
                )
            }
        )
    }

    /**
     * Save theme configs
     * */
    private fun setClockTheme(key: String, value: ClockThemeOptions) {
        with(sp.edit()) {
            putColor(key + themeForegroundKey, value.foreground)
            putColor(key + themeBackgroundKey, value.background)
            putBoolean(key + themeDifferYearKey, value.differYear)
            putColor(key + themeYearColorKey, value.yearColor)
            apply()
        }
    }


    /**
     * Load wallpaper configs
     * */
    private fun getWallpaperOptions(
        key: String,
        default: WallpaperOptions
    ): MutableStateFlow<WallpaperOptions> {

        val enabled = sp.getBoolean(key + wallpaperEnabledKey, default.enabled)
        val zoom = sp.getFloat(key + wallpaperZoomKey, default.zoom)
        val verticalBias = sp.getFloat(key + wallpaperVertBiasKey, default.verticalBias)
        val horizontalBias = sp.getFloat(key + wallpaperHortBiasKey, default.horizontalBias)
        val rotate = sp.getInt(key + wallpaperRotateKey, default.rotate)

        return MutableStateFlow(
            WallpaperOptions(enabled, zoom, verticalBias, horizontalBias, rotate)
        )
    }

    /**
     * Save wallpaper configs
     * */
    private fun setWallpaperOptions(key: String, value: WallpaperOptions) {
        with(sp.edit()) {
            putBoolean(key + wallpaperEnabledKey, value.enabled)
            putFloat(key + wallpaperZoomKey, value.zoom)
            putFloat(key + wallpaperVertBiasKey, value.verticalBias)
            putFloat(key + wallpaperHortBiasKey, value.horizontalBias)
            putInt(key + wallpaperRotateKey, value.rotate)

            apply()
        }
    }
}


/** isPortrait methods wrapper */
internal fun SettingsRepo.getClockLayoutOptions(isPortrait: Boolean) =
    if (isPortrait) layoutPortrait else layoutLandscape

/** isPortrait methods wrapper */
internal fun SettingsRepo.getClockThemeOptions(mode: Int, isPortrait: Boolean) =
    getClockThemeOptions(mode and Configuration.UI_MODE_NIGHT_YES == 0, isPortrait)

/** isPortrait methods wrapper */
internal fun SettingsRepo.getClockThemeOptions(isLight: Boolean, isPortrait: Boolean) =
    if (isLight)
        if (isPortrait) themeLightPortrait else themeDarkPortrait
    else
        if (isPortrait) themeLightLandscape else themeDarkLandscape


/** isPortrait methods wrapper */
internal fun SettingsRepo.setClockLayoutOptions(value: ClockLayoutOptions, isPortrait: Boolean) =
    if (isPortrait) setLayoutPortrait(value) else setLayoutLandscape(value)

/** set theme wrapper */
internal fun SettingsRepo.setClockThemeOptions(
    value: ClockThemeOptions, mode: Int, isPortrait: Boolean
) = setClockThemeOptions(value, mode and Configuration.UI_MODE_NIGHT_YES == 0, isPortrait)

/** set theme wrapper */
internal fun SettingsRepo.setClockThemeOptions(
    value: ClockThemeOptions,
    isLight: Boolean,
    isPortrait: Boolean
) {
    if (isLight)
        if (isPortrait) setThemeLightPortrait(value) else setThemeDarkPortrait(value)
    else
        if (isPortrait) setThemeLightLandscape(value) else setThemeDarkLandscape(value)
}


/**
 * load args color from shared preferences
 * */
private fun SharedPreferences.getColor(key: String, default: Color): Color =
    Color(getInt(key, default.toArgb()))

/**
 * Save color into SharedPreferences
 * */
private fun SharedPreferences.Editor.putColor(key: String, value: Color) =
    putInt(key, value.toArgb())
