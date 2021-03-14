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

package cz.lastaapps.ui.common.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cz.lastaapps.ui.common.extencions.animatedColors
import cz.lastaapps.ui.common.extencions.updateStatusBar


//light theme
private val blue800 = Color(0xff1564bf)
private val blue800Dark = Color(0xff003b8e)
private val redA700 = Color(0xffd50000)
private val redA700Dark = Color(0xFFca0412)
private val grayLight = Color(0xffdddddd)

private val lightColors = lightColors(
    primary = blue800,
    primaryVariant = blue800Dark,
    secondary = redA700,
    secondaryVariant = redA700Dark,
    background = Color.White,
    surface = grayLight,
    error = Color.Red,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
)

//dark theme
private val orange800 = Color(0xffef6c00)
private val orange800Dark = Color(0xffb53d00)
private val blue600 = Color(0xff0083ef)

//private val blue600Dark = Color(0xff0960c9)
private val grayDark = Color(0xFF333333)

private val darkColors = darkColors(
    primary = orange800,
    primaryVariant = orange800Dark,
    secondary = blue600,
    //secondaryVariant = red600Dark,
    background = Color.Black,
    surface = grayDark,
    error = Color.Yellow,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black,
)

@Composable
fun MainTheme(
    lightTheme: Boolean = !isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (lightTheme) lightColors else darkColors
    val animatedColors = animatedColors(colors = colors)

    MaterialTheme(
        colors = animatedColors,
    ) {
        updateStatusBar(lightTheme = lightTheme)

        Surface(content = content, color = MaterialTheme.colors.background)
    }
}
