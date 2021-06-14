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

package cz.lastaapps.president.wallpaper.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.lastaapps.president.wallpaper.R
import cz.lastaapps.ui.common.components.SliderTextField
import kotlin.math.round


@Composable
internal fun ScaleSlider(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
    val options by viewModel.layoutOptions.collectAsState()
    val value = options.scale

    SliderTextField(
        text = stringResource(id = R.string.ui_layout_scale),
        value = value,
        onValueChanged = { viewModel.setLayoutOptions(options.copy(scale = it)) },
        modifier = modifier,
        range = 0.25f..1.75f,
        valueToText = { (round(it * 100) / 100).toString() },
        textToValue = { it.toFloat() }
    )
}

@Composable
internal fun VerticalBiasSlider(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
    val options by viewModel.layoutOptions.collectAsState()
    val value = options.verticalBias

    SliderTextField(
        text = stringResource(id = R.string.ui_layout_vertical),
        value = value,
        onValueChanged = { viewModel.setLayoutOptions(options.copy(verticalBias = it)) },
        modifier = modifier
    )
}

@Composable
internal fun HorizontalBiasSlider(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
    val options by viewModel.layoutOptions.collectAsState()
    val value = options.horizontalBias

    SliderTextField(
        text = stringResource(id = R.string.ui_layout_horizontal),
        value = value,
        onValueChanged = { viewModel.setLayoutOptions(options.copy(horizontalBias = it)) },
        modifier = modifier
    )
}


