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
fun Colors.animated(): Colors =
    Colors(
        primary = animateColorAsState(primary).value,
        primaryVariant = animateColorAsState(primaryVariant).value,
        secondary = animateColorAsState(secondary).value,
        secondaryVariant = animateColorAsState(secondaryVariant).value,
        background = animateColorAsState(background).value,
        surface = animateColorAsState(surface).value,
        error = animateColorAsState(error).value,
        onPrimary = animateColorAsState(onPrimary).value,
        onSecondary = animateColorAsState(onSecondary).value,
        onBackground = animateColorAsState(onBackground).value,
        onSurface = animateColorAsState(onSurface).value,
        onError = animateColorAsState(onError).value,
        isLight = isLight,
    )
