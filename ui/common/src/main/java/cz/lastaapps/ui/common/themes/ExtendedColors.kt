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

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalExtendedColors = compositionLocalOf<ExtendedColors> { error("No default colors") }

data class ExtendedColors(
    val onSurfaceSecondary: Color,
    val waring: Color,
    val onWarning: Color,
    val isLight: Boolean,
)


@Suppress("unused")
val MaterialTheme.extended: ExtendedColors
    @Composable
    get() = LocalExtendedColors.current


internal fun lightExtendedColors(
    onSurfaceSecondary: Color = Color.DarkGray,
    waring: Color = Color(0xffffcc00),
    onWarning: Color = Color.Black,
): ExtendedColors = ExtendedColors(
    onSurfaceSecondary = onSurfaceSecondary,
    waring = waring,
    onWarning = onWarning,
    isLight = true,
)

internal fun darkExtendedColors(
    onSurfaceSecondary: Color = Color.DarkGray,
    waring: Color = Color(0xffffcc00),
    onWarning: Color = Color.Black,
): ExtendedColors = ExtendedColors(
    onSurfaceSecondary = onSurfaceSecondary,
    waring = waring,
    onWarning = onWarning,
    isLight = false,
)

@Composable
internal fun ExtendedColors.animated() =
    ExtendedColors(
        onSurfaceSecondary = animateColorAsState(onSurfaceSecondary).value,
        waring = animateColorAsState(waring).value,
        onWarning = animateColorAsState(onWarning).value,
        isLight = isLight,
    )
