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

package cz.lastaapps.ui.colorpicker

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import cz.lastaapps.ui.common.components.ColorPreview
import cz.lastaapps.ui.common.components.ColorPreviewConstants
import cz.lastaapps.ui.common.components.GeneralDialog
import cz.lastaapps.ui.common.themes.MainTheme

internal val alpha = Color(0xff000000)
internal val red = Color(0xffff0000)
internal val green = Color(0xff00ff00)
internal val blue = Color(0xff0000ff)

@Composable
fun ColorPickerDialog(
    color: Color,
    onColorChanged: (Color) -> Unit,
    shown: Boolean,
    onShownChanged: (Boolean) -> Unit,
    alphaEnabled: Boolean = true,
    title: String = stringResource(R.string.color_picker_dialog_title),
) {
    if (!shown) return

    var tempColor by remember(color) { mutableStateOf(color) }

    GeneralDialog(
        onDismissRequest = { onShownChanged(false) },
        title = {
            Text(title)
        },
        content = {
            ColorPicker(
                color = tempColor,
                onColorChanged = { tempColor = it },
                modifier = Modifier.padding(8.dp),
                alphaEnabled = alphaEnabled,
                showPreview = true,
            )
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            ) {
                TextButton(
                    onClick = { onShownChanged(false) }
                ) {
                    Text(
                        stringResource(R.string.color_picker_dialog_cancel),
                        textAlign = TextAlign.Center,
                    )
                }
                Button(
                    onClick = {
                        onColorChanged(tempColor)
                        onShownChanged(false)
                    }
                ) {
                    Text(
                        stringResource(R.string.color_picker_dialog_ok),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MainTheme(true) {
        var color by remember {
            mutableStateOf(Color.Red)
        }
        ColorPicker(color = color, onColorChanged = { color = it })
    }
}

@Composable
fun ColorPicker(
    color: Color,
    onColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier,
    alphaEnabled: Boolean = true,
    showHex: Boolean = true,
    showPreview: Boolean = false,
) {
    Column(modifier = modifier) {

        if (alphaEnabled)
            ColorSlider(alpha, color.alpha, { onColorChanged(color.copy(alpha = it)) })
        ColorSlider(red, color.red, { onColorChanged(color.copy(red = it)) })
        ColorSlider(green, color.green, { onColorChanged(color.copy(green = it)) })
        ColorSlider(blue, color.blue, { onColorChanged(color.copy(blue = it)) })

        BoxWithConstraints {

            val spacing = 8.dp
            val colorPreviewSize = ColorPreviewConstants.size
            val textSize =
                max(0.dp, if (showPreview) maxWidth - spacing - colorPreviewSize else maxWidth)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.width(textSize)
                ) {
                    if (showHex)
                        HexField(
                            modifier = Modifier.width(textSize),
                            color = color,
                            onColorChanged = { onColorChanged(it) },
                            alphaEnabled = alphaEnabled
                        )
                }

                if (showPreview)
                    ColorPreview(
                        color = color,
                        preferredSize = colorPreviewSize
                    )
            }
        }
    }
}

@Composable
internal fun ColorSlider(
    sliderColor: Color,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            steps = 256 + 1,
            colors = SliderDefaults.colors(
                thumbColor = sliderColor,
                activeTrackColor = sliderColor,
                activeTickColor = Color.Transparent,
                //inactiveTrackColor = ,
                inactiveTickColor = Color.Transparent,
            ),
        )
    }
}
