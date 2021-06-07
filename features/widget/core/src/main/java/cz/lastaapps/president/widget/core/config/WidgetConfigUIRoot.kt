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

import android.appwidget.AppWidgetManager
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.battery.BatteryWarning
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.core.R
import cz.lastaapps.ui.common.layouts.BorderLayout
import cz.lastaapps.ui.common.layouts.WidthLimitingLayout

/**
 * The Widget options UI
 * */
@Composable
internal fun WidgetConfigRoot(
    widgetId: Int,
    onSuccess: () -> Unit,
    onCancel: () -> Unit,
    viewModel: WidgetViewModel,
    components: @Composable (
        state: WidgetState,
        onStateChanged: (WidgetState) -> Unit,
        isLightPreview: Boolean,
        onLightPreviewChanged: (Boolean) -> Unit,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {

    //puts widgetId into a viewModel
    remember {
        viewModel.setWidgetId(widgetId)
        null
    }

    //stops activity when core customization task is finished
    val uiStage by viewModel.configProgressState.collectAsState()
    remember(uiStage) {
        when (uiStage) {
            UIStage.OK -> onSuccess()
            UIStage.CANCEL -> onCancel()
        }
        null
    }

    //loads data representing UI state
    val isLightPreview by viewModel.getIsLightPreview().collectAsState()
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

                val (previewConst, settConst, batteryConst) = createRefs()

                //previews the core
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
                    viewModel = viewModel,
                )

                Card(
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        //set's max height so preview is shows no matter what
                        .sizeIn(maxHeight = constraintLayoutHeight * .75f)
                        .constrainAs(settConst) {
                            bottom.linkTo(parent.bottom)
                            centerHorizontallyTo(parent)

                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent
                        },
                ) {
                    BorderLayout(
                        modifier = Modifier.padding(8.dp),
                        center = {
                            //all the core options
                            SettingsContent(
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                state = widgetState,
                                onStateChanged = { viewModel.setState(it) },
                                isLightPreview = isLightPreview,
                                components = components,
                                onLightPreviewChanged = { viewModel.setIsLightPreview(it) }
                            )
                        },
                        bottom = {
                            ConfirmButtons(viewModel)
                        },
                        spaceBy = 8.dp,
                    )
                }

                BatteryWarning(
                    modifier = Modifier.constrainAs(batteryConst) {
                        top.linkTo(parent.top, 32.dp)
                        end.linkTo(parent.end, 32.dp)
                    },
                )
            }
        }
    }
}

/**
 * Shows preview of the core with current time left and theme given
 * */
@Composable
private fun WidgetPreview(
    isLightPreview: Boolean,
    theme: WidgetState,
    viewModel: WidgetViewModel,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    val state by CurrentState.getCurrentState(scope).collectAsState()

    val maxPreviewHeight = 128.dp
    val previewRatio = viewModel.viewUpdater.preferredAspectRation

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

        height = min(height, maxPreviewHeight)
        width = height * previewRatio

        val context = LocalContext.current
        val views = remember(context) {
            //AppWidgetManager.INVALID_APPWIDGET_ID disables the preview - prevents clicks
            viewModel.viewUpdater.createRemoteViews(context, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        remember(state) {
            viewModel.viewUpdater.updateState(context, views, state)
        }

        remember(isLightPreview, theme) {
            viewModel.viewUpdater.updateColors(isLightPreview, views, theme, null)
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
    components: @Composable (
        state: WidgetState,
        onStateChanged: (WidgetState) -> Unit,
        isLightPreview: Boolean,
        onLightPreviewChanged: (Boolean) -> Unit,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        components(state, onStateChanged, isLightPreview, onLightPreviewChanged)
    }
}

/**
 * Confirms or cancels the whole process
 * propagates events into viewModel
 * */
@Composable
private fun ConfirmButtons(
    viewModel: WidgetViewModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        //buttons are disabled when the data are processed to prevent multiple calls
        val uiStage by viewModel.configProgressState.collectAsState()
        val enabled = remember(uiStage) {
            uiStage != UIStage.PROCESSING
        }

        TextButton(
            onClick = { viewModel.cancel() },
            modifier = Modifier.weight(.5f),
            enabled = enabled,
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = { viewModel.ok() },
            modifier = Modifier.weight(.5f),
            enabled = enabled,
        ) {
            Text(
                text = stringResource(id = R.string.ok),
                textAlign = TextAlign.Center,
            )
        }
    }
}

