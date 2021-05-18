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

package cz.lastaapps.president.widget.core.config

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.lastaapps.president.widget.core.R
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.settings.ColorPickerSetting
import cz.lastaapps.ui.settings.SettingsGroup
import cz.lastaapps.ui.settings.SettingsGroupColumn
import cz.lastaapps.ui.settings.SwitchSettings
import kotlin.math.round


/**
 * Selection what theme is actually shown to a user on a home screen
 * follow system, day only and night options presented
 * */
@Composable
fun UIModeSelection(
    state: WidgetState,
    onStateChanged: (WidgetState) -> Unit,
    modifier: Modifier = Modifier,
) {

    //current slider position
    var position by remember(state) {
        mutableStateOf(
            when (state.theme) {
                WidgetThemeMode.DAY -> 0
                WidgetThemeMode.SYSTEM -> 1
                WidgetThemeMode.NIGHT -> 2
                else -> throw IllegalArgumentException("Unknown Widget theme ${state.theme}")
            }.toFloat()
        )
    }

    SettingsGroupColumn(modifier) {
        //labels above the slider
        Row {
            Text(
                stringResource(id = R.string.mode_light),
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f / 3)
            )
            Text(
                stringResource(id = R.string.mode_system),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f / 3)
            )
            Text(
                stringResource(id = R.string.mode_dark),
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f / 3)
            )
        }
        //slider with 3 states only
        Slider(
            value = position,
            onValueChange = { position = it },
            onValueChangeFinished = {
                val newTheme = when (round(position).toInt()) {
                    0 -> WidgetThemeMode.DAY
                    1 -> WidgetThemeMode.SYSTEM
                    2 -> WidgetThemeMode.NIGHT
                    else -> throw IllegalStateException("Illegal slider position $position")
                }
                onStateChanged(state.copy(theme = newTheme))
            },
            steps = 1,
            valueRange = 0f..2f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Yellow,
                activeTrackColor = Color.Yellow,
                inactiveTrackColor = Color.Black,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            )
        )
    }
}

/**
 * Sets colors and let's user select day/night preview mode
 * */
@Composable
fun ThemedOptions(
    state: WidgetState,
    onStateChanged: (WidgetState) -> Unit,
    isLightPreview: Boolean,
    onLightPreviewChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val tab by remember(isLightPreview) { mutableStateOf(if (isLightPreview) 0 else 1) }
    val titles = listOf(
        stringResource(R.string.preview_light),
        stringResource(R.string.preview_dark),
    )

    Card(backgroundColor = MaterialTheme.colors.surface, modifier = modifier) {
        Column {
            TabRow(selectedTabIndex = tab) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tab == index,
                        onClick = { onLightPreviewChanged(index == 0) }
                    )
                }
            }

            val foreground = state.getForeground(isLightPreview)
            val background = state.getBackground(isLightPreview)

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                //only one color picker can be expanded at the time
                var expanded by rememberMutableSaveable {
                    mutableStateOf(false to false)
                }

                //picks the foreground color
                ColorPickerSetting(
                    text = stringResource(R.string.foreground),
                    color = Color(foreground),
                    onColorChanged = {
                        onStateChanged(
                            if (isLightPreview) {
                                state.copy(lightForeground = it.toArgb())
                            } else {
                                state.copy(darkForeground = it.toArgb())
                            }
                        )
                    },
                    expanded = expanded.first,
                    onExpandedChanged = {
                        expanded = if (expanded.first) {
                            false to false
                        } else {
                            true to false
                        }
                    },
                    alphaEnabled = false,
                    hexEnabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                //background color picker
                ColorPickerSetting(
                    text = stringResource(R.string.background),
                    color = Color(background),
                    onColorChanged = {
                        onStateChanged(
                            if (isLightPreview) {
                                state.copy(lightBackground = it.toArgb())
                            } else {
                                state.copy(darkBackground = it.toArgb())
                            }
                        )
                    },
                    expanded = expanded.second,
                    onExpandedChanged = {
                        expanded = if (expanded.second) {
                            false to false
                        } else {
                            false to true
                        }
                    },
                    alphaEnabled = true,
                    hexEnabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun EnableFrameSwitch(
    state: WidgetState,
    onStateChanged: (WidgetState) -> Unit,
    modifier: Modifier = Modifier,
) {

    val isFrameEnabled = state.frameEnabled

    SettingsGroup(
        modifier = modifier,
    ) {
        SwitchSettings(
            text = stringResource(id = R.string.frame_enabled),
            checked = isFrameEnabled,
            onChange = {
                onStateChanged(state.copy(frameEnabled = !isFrameEnabled))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * If the years digit should have different color for the theme selected
 * */
@Composable
fun DifferYear(
    state: WidgetState,
    onStateChanged: (WidgetState) -> Unit,
    isLightPreview: Boolean,
    modifier: Modifier = Modifier
) {
    SettingsGroupColumn(modifier = modifier.animateContentSize()) {
        SwitchSettings(
            text = stringResource(id = R.string.differ_year),
            checked = state.isDifferYear(isLightPreview),
            onChange = {
                val newState = !state.isDifferYear(isLightPreview)
                onStateChanged(
                    if (isLightPreview)
                        state.copy(lightDifferYear = newState)
                    else
                        state.copy(darkDifferYear = newState)
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )

        //let's user select different color if the option is enabled
        if (state.isDifferYear(isLightPreview)) {

            var expanded by remember { mutableStateOf(false) }

            ColorPickerSetting(
                text = stringResource(R.string.background),
                color = Color(state.getYearColor(isLightPreview)!!),
                onColorChanged = {
                    onStateChanged(
                        if (isLightPreview) {
                            state.copy(lightYearColor = it.toArgb())
                        } else {
                            state.copy(darkYearColor = it.toArgb())
                        }
                    )
                },
                expanded = expanded,
                onExpandedChanged = { expanded = !expanded },
                alphaEnabled = false,
                hexEnabled = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}