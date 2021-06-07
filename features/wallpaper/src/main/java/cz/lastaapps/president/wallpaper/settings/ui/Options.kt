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
import androidx.compose.animation.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.president.wallpaper.R
import cz.lastaapps.president.wallpaper.service.PresidentWallpaperService
import cz.lastaapps.president.wallpaper.settings.help.WallpaperHelpIcon
import cz.lastaapps.ui.common.components.TextSwitch
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.layouts.ExpandableBottomLayout
import cz.lastaapps.ui.common.layouts.FlexRow
import cz.lastaapps.ui.settings.*


/**
 * Root with all the settings for a wallpaper
 * */
@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun WallpaperOptionsLayout(modifier: Modifier = Modifier) {

    val backgroundAlpha = .7f
    val sideMargins = 8.dp

    var expanded by rememberMutableSaveable { mutableStateOf(true) }

    ConstraintLayout(modifier = modifier.padding(sideMargins)) {

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
            Switches(transparency = backgroundAlpha)
        }

        val maxHeightGuide = createGuidelineFromTop(.3f)
        val scroll = rememberScrollState()

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

            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Bottom)
            ) {

                val maxWidthMod = Modifier.fillMaxWidth()

                SettingsGroup(backgroundAlpha = backgroundAlpha) {
                    RotationFixSwitch(maxWidthMod)
                    UIModeSelection(maxWidthMod)
                }

                SettingsGroup(backgroundAlpha = backgroundAlpha) {
                    ScaleSlider(maxWidthMod)
                    VerticalBiasSlider(maxWidthMod)
                    HorizontalBiasSlider(maxWidthMod)
                }

                SettingsGroup(backgroundAlpha = backgroundAlpha) {
                    Pickers(maxWidthMod)
                }

                SettingsGroup(backgroundAlpha = backgroundAlpha) {
                    WallpaperConfigs(maxWidthMod)
                }
            }
        }
    }
}

/**
 * Preview options in the top of a screen
 * */
@Composable
private fun Switches(
    modifier: Modifier = Modifier,
    transparency: Float = 1f,
) {
    SettingsGroup(
        modifier = modifier,
        border = BorderStroke(0.dp, Color.Transparent),
        backgroundAlpha = transparency,
    ) {
        FlexRow(
            rowsVerticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            itemInBoxAlignment = Alignment.CenterVertically,
            horizontalItemsBoxInRowArrangement = Arrangement.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                ThemeSwitch()
                OrientationSwitch()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SetAsWallpaper()
                WallpaperHelpIcon()
            }
        }
    }
}

/**
 * Changes preview theme
 * */
@Composable
private fun ThemeSwitch(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
    val isDay by viewModel.isDayPreview.collectAsState()

    TextSwitch(
        text = stringResource(id = if (isDay) R.string.ui_theme_day else R.string.ui_theme_night),
        value = isDay,
        onCheckedChange = { viewModel.setIsDay(it) },
        modifier = modifier,
    )
}

/**
 * changes the preview orientation
 * */
@Composable
private fun OrientationSwitch(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
    val isPortrait by viewModel.isPortrait.collectAsState()

    TextSwitch(
        text = stringResource(if (isPortrait) R.string.ui_portrait else R.string.ui_landscape),
        value = isPortrait,
        onCheckedChange = { viewModel.setIsPortrait(it) },
        modifier = modifier,
    )
}

/**
 * Show system set as wallpaper dialog
 * */
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
        modifier = modifier,
    ) {
        Icon(
            Icons.Default.OpenInNew,
            contentDescription = stringResource(R.string.content_description_set_wallpaper)
        )
    }
}

/**
 * On some devices landscape mode doesn't behave correctly - fix can be enabled
 * */
@Composable
private fun RotationFixSwitch(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
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

/**
 * Describes what rotation fix is
 * */
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
                    Text(
                        stringResource(id = R.string.ui_rotation_help_understand),
                        textAlign = TextAlign.Center,
                    )
                }
            },
            text = {
                Text(stringResource(id = R.string.ui_rotation_help_text))
            },
        )
    }
}

/**
 * Selects theme of wallpaper - system, light, dark
 * */
@Composable
private fun UIModeSelection(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
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

/**
 * Foreground and background color pickers
 * */
@Composable
private fun Pickers(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {

        var expanded by rememberMutableSaveable {
            mutableStateOf(0)
        }

        ForegroundPicker(
            expanded = expanded == 1,
            onExpendedRequest = {
                expanded = if (expanded != 1) 1 else 0
            },
            modifier = Modifier.fillMaxWidth(),
        )

        BackgroundPicker(
            expanded = expanded == 2,
            onExpendedRequest = {
                expanded = if (expanded != 2) 2 else 0
            },
            modifier = Modifier.fillMaxWidth(),
        )

        DifferYear(Modifier.fillMaxWidth())
    }
}

/**
 * Choose text color
 * */
@Composable
private fun ForegroundPicker(
    expanded: Boolean,
    onExpendedRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel = wallpaperViewModel()
    val options by viewModel.themeOptions.collectAsState()
    val color = options.foreground

    ColorPickerSetting(
        text = stringResource(id = R.string.ui_foreground),
        color = color,
        onColorChanged = { viewModel.setThemeOptions(options.copy(foreground = it)) },
        modifier = modifier,
        alphaEnabled = true,
        expanded = expanded,
        onExpandedChanged = { onExpendedRequest() },
    )
}

/**
 * Choose background color
 * */
@Composable
private fun BackgroundPicker(
    expanded: Boolean,
    onExpendedRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel = wallpaperViewModel()
    val options by viewModel.themeOptions.collectAsState()
    val color = options.background

    ColorPickerSetting(
        text = stringResource(id = R.string.ui_background),
        color = color,
        onColorChanged = { viewModel.setThemeOptions(options.copy(background = it)) },
        modifier = modifier,
        alphaEnabled = false,
        expanded = expanded,
        onExpandedChanged = { onExpendedRequest() },
    )
}

/**
 * If year digit should have different color
 * */
@Composable
private fun DifferYear(modifier: Modifier = Modifier) {

    val viewModel = wallpaperViewModel()
    val options by viewModel.themeOptions.collectAsState()
    val diffEnabled = options.differYear
    val yearColor = options.yearColor

    Column(modifier.animateContentSize()) {

        SwitchSettings(
            text = stringResource(id = R.string.ui_differ_year),
            checked = diffEnabled,
            onChange = { viewModel.setThemeOptions(options.copy(differYear = !diffEnabled)) },
            modifier = Modifier.fillMaxWidth(),
        )

        if (diffEnabled) {

            var expanded by rememberMutableSaveable { mutableStateOf(false) }

            ColorPickerSetting(
                text = stringResource(id = R.string.ui_differ_year_color),
                color = yearColor,
                onColorChanged = { viewModel.setThemeOptions(options.copy(yearColor = it)) },
                modifier = Modifier.fillMaxWidth(),
                alphaEnabled = true,
                expanded = expanded,
                onExpandedChanged = { expanded = !expanded },
            )
        }
    }
}

