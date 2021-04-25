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

package cz.lastaapps.ui.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max


@Composable
fun SliderTextField(
    value: Float,
    onValueChanged: ((Float) -> Unit),
    modifier: Modifier = Modifier,
    text: String? = null,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    valueToText: (Float) -> String = { (it * 100).toInt().toString() },
    textToValue: (String) -> Float = { (it.toFloat() / 100) },
    smartZeroInput: Boolean = true,
    textFieldSize: Dp = 64.dp,
) {
    BoxWithConstraints(modifier = modifier) {

        val spacing = 8.dp
        val sliderWidth = max(0.dp, maxWidth - textFieldSize - spacing)

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.width(sliderWidth),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            ) {
                text?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body2,
                    )
                }
                Slider(
                    value = value,
                    onValueChange = { onValueChanged(it) },
                    valueRange = range,
                )
            }

            OutlinedTextField(
                value = valueToText(value),
                onValueChange = {
                    onValueChanged(
                        try {
                            if (it == "") range.start
                            else {
                                val numberValue = if (!(smartZeroInput && value == 0f)) {
                                    textToValue(it)
                                } else {
                                    textToValue(it) / 10
                                }

                                when (numberValue) {
                                    in range.start..range.endInclusive -> numberValue
                                    in Float.MIN_VALUE..range.start -> range.start
                                    else -> range.endInclusive
                                }
                            }
                        } catch (e: Exception) {
                            //protects from a wrong input
                            e.printStackTrace()
                            range.start
                        }
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.width(textFieldSize)
            )
        }
    }
}