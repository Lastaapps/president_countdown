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

package cz.lastaapps.ui.common.extencions

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Creates random RGB color
 * */
@Composable
fun randomColor(): Color {
    val random = Random.Default
    return remember { Color(random.nextFloat(), random.nextFloat(), random.nextFloat()) }
}

fun Color.inverted(): Color {
    return Color(alpha, 1 - red, 1 - green, 1 - blue)
}

/**
 * Animates colors between states - themes and ui modes
 * */
@Composable
fun animatedColors(colors: Colors): Colors =
    Colors(
        primary = animateColorAsState(colors.primary).value,
        primaryVariant = animateColorAsState(colors.primaryVariant).value,
        secondary = animateColorAsState(colors.secondary).value,
        secondaryVariant = animateColorAsState(colors.secondaryVariant).value,
        background = animateColorAsState(colors.background).value,
        surface = animateColorAsState(colors.surface).value,
        error = animateColorAsState(colors.error).value,
        onPrimary = animateColorAsState(colors.onPrimary).value,
        onSecondary = animateColorAsState(colors.onSecondary).value,
        onBackground = animateColorAsState(colors.onBackground).value,
        onSurface = animateColorAsState(colors.onSurface).value,
        onError = animateColorAsState(colors.onError).value,
        colors.isLight,
    )
