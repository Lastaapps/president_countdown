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

package cz.lastaapps.ui.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cz.lastaapps.ui.colorpicker.ColorPicker
import cz.lastaapps.ui.colorpicker.ColorPickerDialog
import cz.lastaapps.ui.common.components.ColorPreview
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable


/**
 * Shows color picker by expanding downward and previews currently selected color
 * */
@Composable
fun ColoPickerSettings(
    text: String,
    color: Color,
    onColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier,
    alphaEnabled: Boolean = true,
) {
    var shown by rememberMutableSaveable { mutableStateOf(false) }

    Column(modifier.animateContentSize()) {

        CustomSettings(
            text = text,
            onClick = {
                shown = !shown
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            ColorPreview(color = color)
        }

        if (shown) {
            ColorPicker(color, onColorChanged, alphaEnabled = alphaEnabled)
        }
    }
}

/**
 * Shows color picker dialog and previews currently selected color
 * */
@Composable
fun ColoPickerDialogSettings(
    text: String,
    color: Color,
    onColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier,
    alphaEnabled: Boolean = true,
) {
    var shown by rememberMutableSaveable { mutableStateOf(false) }

    CustomSettings(
        text = text,
        onClick = {
            shown = !shown
        },
        modifier = modifier,
    ) {
        ColorPreview(color = color)
    }

    ColorPickerDialog(
        color = color,
        onColorChanged = onColorChanged,
        shown = shown,
        onShownChanged = { shown = false },
        alphaEnabled = alphaEnabled,
    )
}

