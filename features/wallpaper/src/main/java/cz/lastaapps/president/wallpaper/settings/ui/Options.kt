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

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.president.wallpaper.R
import cz.lastaapps.president.wallpaper.service.PresidentWallpaperService
import cz.lastaapps.ui.common.components.TextSwitch
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.layouts.ExpandableBottomLayout
import cz.lastaapps.ui.settings.*


@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun WallpaperOptionsLayout(modifier: Modifier = Modifier) {

    CompositionLocalProvider(LocalContentAlpha provides .7f) {

        val sideMargins = 8.dp

        var expanded by rememberMutableSaveable { mutableStateOf(true) }

        ConstraintLayout(modifier = modifier.padding(sideMargins)) {

            val viewModel = viewModel()
            val res = LocalContext.current.resources.configuration
            remember(res) {
                viewModel.setIsLand(res.orientation % 2 == 1)
            }

            val (switchesConst, settingsConst) = createRefs()

            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically({ it * -2 }),
                exit = slideOutVertically({ it * -2 }),
                modifier = Modifier.constrainAs(switchesConst) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                },
            ) {
                Switches()
            }

            val maxHeightGuide = createGuidelineFromTop(.3f)

            ExpandableBottomLayout(
                expanded = expanded,
                onExpanded = { expanded = it },
                modifier = Modifier.constrainAs(settingsConst) {
                    top.linkTo(maxHeightGuide)
                    bottom.linkTo(parent.bottom, sideMargins)
                    centerHorizontallyTo(parent)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            ) {
                val scroll = rememberScrollState()

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .verticalScroll(scroll),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    val groupSpading = Modifier.padding(top = (8 / 2).dp, bottom = (8 / 2).dp)
                    val maxWidthMod = Modifier.fillMaxWidth()

                    SettingsGroupColumn(
                        modifier = groupSpading,
                        border = BorderStroke(0.dp, Color.Transparent),
                    ) {
                        RotationFixSwitch(maxWidthMod)
                        UIModeSelection(maxWidthMod)
                    }

                    SettingsGroupColumn(
                        modifier = groupSpading,
                        border = BorderStroke(0.dp, Color.Transparent),
                    ) {
                        ScaleSlider(maxWidthMod)
                        VerticalBiasSlider(maxWidthMod)
                        HorizontalBiasSlider(maxWidthMod)
                    }

                    SettingsGroupColumn(
                        modifier = groupSpading,
                        border = BorderStroke(0.dp, Color.Transparent),
                    ) {
                        ForegroundPicker(maxWidthMod)
                        BackgroundPicker(maxWidthMod)
                    }
                }
            }
        }
    }
}

@Composable
private fun Switches(modifier: Modifier = Modifier) {
    SettingsGroup(
        modifier = modifier,
        border = BorderStroke(0.dp, Color.Transparent),
    ) {
        Row {
            val alignCenterModifier = Modifier.align(Alignment.CenterVertically)
            ThemeSwitch(alignCenterModifier)
            OrientationSwitch(alignCenterModifier)
            SetAsWallpaper(alignCenterModifier)
        }
    }
}

@Composable
private fun ThemeSwitch(modifier: Modifier = Modifier) {

    val viewModel = viewModel()
    val isDay by viewModel.isDayPreview.collectAsState()

    TextSwitch(
        text = stringResource(id = if (isDay) R.string.ui_theme_day else R.string.ui_theme_night),
        value = isDay,
        onCheckedChange = { viewModel.setIsDay(it) },
        modifier
    )
}

@Composable
private fun OrientationSwitch(modifier: Modifier = Modifier) {

    val viewModel = viewModel()
    val isPortrait by viewModel.orientationPortrait.collectAsState()

    TextSwitch(
        text = stringResource(if (isPortrait) R.string.ui_portrait else R.string.ui_landscape),
        value = isPortrait,
        onCheckedChange = { viewModel.setOrientation(it) },
        modifier
    )
}

@Composable
private fun SetAsWallpaper(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(context, PresidentWallpaperService::class.java)
            )
            context.startActivity(intent)
        },
        modifier = modifier
    ) {
        Icon(
            Icons.Default.OpenInNew,
            contentDescription = stringResource(R.string.content_description_set_wallpaper)
        )
    }
}

@Composable
private fun RotationFixSwitch(modifier: Modifier = Modifier) {

    val viewModel = viewModel()
    val state by viewModel.rotationFix.collectAsState()

    var showDialog by rememberMutableSaveable { mutableStateOf(false) }

    RotationFixHelpDialog(showDialog) { showDialog = it }

    val onClick = {
        viewModel.setRotationFix(!state)
    }

    CustomSettings(
        modifier = modifier,
        onClick = onClick,
        title = {
            Row {
                SettingsTitle(
                    text = stringResource(id = R.string.ui_rotation_fix),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = stringResource(id = R.string.content_description_hide)
                    )
                }
            }
        },
        content = {
            Switch(
                checked = state,
                onCheckedChange = { onClick() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primaryVariant,
                    checkedTrackColor = MaterialTheme.colors.primary,
                    uncheckedThumbColor = MaterialTheme.colors.secondary,
                    uncheckedTrackColor = MaterialTheme.colors.onSecondary,
                )
            )
        },
        useDivider = false,
    )
}

@Composable
private fun RotationFixHelpDialog(showDialog: Boolean, onStateChanged: (Boolean) -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onStateChanged(false)
            },
            title = {
                Text(stringResource(id = R.string.ui_rotation_help_title))
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        onStateChanged(false)
                    },
                ) {
                    Text(stringResource(id = R.string.ui_rotation_help_understand))
                }
            },
            text = {
                Text(stringResource(id = R.string.ui_rotation_help_text))
            },
        )
    }
}

@Composable
private fun UIModeSelection(modifier: Modifier = Modifier) {

    val viewModel = viewModel()
    val selectedMode by remember { viewModel.uiMode }.collectAsState()

    DropdownSettings(
        text = stringResource(id = R.string.ui_wallpaper_theme),
        items = listOf(
            Configuration.UI_MODE_NIGHT_NO,
            Configuration.UI_MODE_NIGHT_YES,
            Configuration.UI_MODE_NIGHT_UNDEFINED
        ),
        selected = selectedMode,
        onItemSelected = { viewModel.setUIMode(it) },
        itemsText = { uiStateName(state = it) },
        modifier = modifier,
    )
}

@Composable
private fun uiStateName(state: Int): String {
    return stringResource(
        id = when {
            state and Configuration.UI_MODE_NIGHT_YES > 0 -> R.string.ui_night
            state and Configuration.UI_MODE_NIGHT_NO > 0 -> R.string.ui_day
            else -> R.string.ui_system
        }
    )
}

@Composable
private fun ForegroundPicker(modifier: Modifier = Modifier) {

    val viewModel = viewModel()
    val color by remember { viewModel.foregroundColor }.collectAsState()

    ColorPickerDialogSetting(
        text = stringResource(id = R.string.ui_foreground),
        color = color,
        onColorChanged = { viewModel.setForegroundColor(it) },
        modifier = modifier,
        alphaEnabled = true,
    )
}

@Composable
private fun BackgroundPicker(modifier: Modifier = Modifier) {

    val viewModel = viewModel()
    val color by remember { viewModel.backgroundColor }.collectAsState()

    ColorPickerDialogSetting(
        text = stringResource(id = R.string.ui_background),
        color = color,
        onColorChanged = { viewModel.setBackgroundColor(it) },
        modifier = modifier,
        alphaEnabled = false,
    )
}

