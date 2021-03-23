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

package cz.lastaapps.president.widget.config

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.R
import cz.lastaapps.president.widget.widget.RemoteViewUpdater
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.extencions.viewModelKt
import cz.lastaapps.ui.common.layouts.WidthLimitingLayout
import cz.lastaapps.ui.settings.ColorPickerSetting
import cz.lastaapps.ui.settings.SettingsGroupColumn
import cz.lastaapps.ui.settings.SwitchSettings
import kotlin.math.round

/**
 * The Widget options UI
 * */
@Composable
internal fun WidgetConfigRoot(
    widgetId: Int,
    onSuccess: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {

    //puts widgetId into a viewModel
    val viewModel = viewModel()
    remember {
        viewModel.setWidgetId(widgetId)
        null
    }

    //stops activity when widget customization task is finished
    val uiStage by viewModel.uiState.collectAsState()
    remember(uiStage) {
        when (uiStage) {
            UIStage.OK -> onSuccess()
            UIStage.CANCEL -> onCancel()
        }
        null
    }

    //loads data representing UI state
    var isLightPreview by rememberMutableSaveable { mutableStateOf(true) }
    val widgetState by viewModel.getState().collectAsState()

    //limits max width
    WidthLimitingLayout(
        modifier = Modifier
            .padding(8.dp)
            .then(modifier)
    ) {

        //used to measure app's screen size
        BoxWithConstraints {

            //protects settings from being to big
            val constraintLayoutHeight = maxHeight

            ConstraintLayout(Modifier.fillMaxSize()) {

                val (previewConst, settConst) = createRefs()

                //previews the widget
                WidgetPreview(
                    isLightPreview = isLightPreview, theme = widgetState,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .constrainAs(previewConst) {
                            top.linkTo(parent.top)
                            bottom.linkTo(settConst.top)
                            centerHorizontallyTo(parent)

                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                )

                //all the widget options
                val scroll = rememberScrollState()
                SettingsContent(
                    modifier = Modifier
                        //set's max height so preview is shows no matter what
                        .sizeIn(maxHeight = constraintLayoutHeight * .75f)
                        .verticalScroll(scroll)
                        .constrainAs(settConst) {
                            bottom.linkTo(parent.bottom)
                            centerHorizontallyTo(parent)

                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent
                        },
                    state = widgetState,
                    onStateChanged = { viewModel.setState(it) },
                    isLightPreview = isLightPreview,
                    onLightPreviewChanged = { isLightPreview = it }
                )
            }
        }
    }
}

/**
 * Shows preview of the widget with current time left and theme given
 * */
@Composable
private fun WidgetPreview(
    isLightPreview: Boolean,
    theme: WidgetState,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    val state by CurrentState.getCurrentState(scope).collectAsState()

    val maxPreviewWidth = 512.dp
    val previewRatio = 4f / 1

    BoxWithConstraints(modifier) {
        var width: Dp
        var height: Dp

        if (maxWidth / maxHeight > previewRatio) {
            height = maxHeight
            width = height * previewRatio
        } else {
            width = maxWidth
            height = width / previewRatio
        }

        width = min(width, maxPreviewWidth)
        height = width / previewRatio

        val context = LocalContext.current
        val views = remember(context) {
            RemoteViewUpdater.createRemoteViews(context)
        }

        remember(state) {
            RemoteViewUpdater.updateState(context, views, state)
        }

        remember(isLightPreview, theme) {
            RemoteViewUpdater.updateColors(isLightPreview, views, theme)
            null
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            // Adds view to Compose
            AndroidView(
                modifier = Modifier.size(width, height),
                factory = { viewContext ->
                    FrameLayout(viewContext).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER,
                        )
                    }
                },
                update = { view ->

                    val child: View = views.apply(view.context, view)
                    view.removeAllViews()
                    view.addView(child)
                }
            )
        }
    }
}

/**
 * Layout with all the options
 * */
@Composable
private fun SettingsContent(
    state: WidgetState,
    onStateChanged: (WidgetState) -> Unit,
    isLightPreview: Boolean,
    onLightPreviewChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            UIModeSelection(
                state = state,
                onStateChanged = onStateChanged,
            )
            ThemedOptions(
                state = state,
                onStateChanged = onStateChanged,
                isLightPreview = isLightPreview,
                onLightPreviewChanged = onLightPreviewChanged,
            )
            DifferYear(
                state = state,
                onStateChanged = onStateChanged,
                isLightPreview = isLightPreview,
                modifier = Modifier.fillMaxWidth(),
            )
            ConfirmButtons(
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Selection what theme is actually shown to a user on a homescreen
 * follow system, day only and night options presented
 * */
@Composable
private fun UIModeSelection(
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
            //TODO resolve after Compose version upgrade
            //onChangeFinished is not updated in the Slider
            onValueChangeFinished = {
                val newTheme = when (round(position).toInt()) {
                    0 -> WidgetThemeMode.DAY
                    1 -> WidgetThemeMode.SYSTEM
                    2 -> WidgetThemeMode.NIGHT
                    else -> throw IllegalStateException("Illegal slider position $position")
                }
                //onStateChanged(state.copy(theme = newTheme))
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
private fun ThemedOptions(
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

/**
 * If the years digit should have different color for the theme selected
 * */
@Composable
private fun DifferYear(
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

/**
 * Confirms or cancels the whole process
 * propagates events into viewModel
 * */
@Composable
private fun ConfirmButtons(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val viewModel = viewModel()

        //buttons are disabled when the data are processed to prevent multiple calls
        val uiStage by viewModel.uiState.collectAsState()
        val enabled = remember(uiStage) {
            uiStage != UIStage.PROCESSING
        }

        TextButton(
            onClick = { viewModel.cancel() },
            modifier = Modifier.weight(.5f),
            enabled = enabled,
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }
        Button(
            onClick = { viewModel.ok() },
            modifier = Modifier.weight(.5f),
            enabled = enabled,
        ) {
            Text(text = stringResource(id = R.string.ok))
        }
    }
}

@Composable
private fun viewModel() = viewModelKt(WidgetViewModel::class)

