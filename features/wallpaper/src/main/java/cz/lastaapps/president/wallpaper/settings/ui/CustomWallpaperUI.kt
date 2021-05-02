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

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.lastaapps.president.wallpaper.R
import cz.lastaapps.president.wallpaper.settings.WallpaperViewModel
import cz.lastaapps.president.wallpaper.settings.files.RequestStoragePermissionDialog
import cz.lastaapps.president.wallpaper.settings.files.hasStoragePermissionForFiles
import cz.lastaapps.president.wallpaper.settings.files.hasStoragePermissionForSystem
import cz.lastaapps.ui.common.components.IconTextRow
import cz.lastaapps.ui.common.components.SliderTextField
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.extencions.viewModelKt
import cz.lastaapps.ui.settings.SwitchSettings
import kotlin.math.round

/**
 * Wallpaper image background configs
 * */
@Composable
internal fun WallpaperConfigs(modifier: Modifier = Modifier) {

    val viewModel = viewModelKt(WallpaperViewModel::class)
    val wallpaperOptions by viewModel.wallpaperOptions.collectAsState()
    val enabled = wallpaperOptions.enabled

    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SwitchSettings(
            text = stringResource(id = R.string.ui_wallpaper_enabled),
            checked = enabled,
            onChange = { viewModel.setWallpaperOptions(wallpaperOptions.copy(enabled = !enabled)) },
            modifier = Modifier.fillMaxWidth(),
        )

        if (enabled) {
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                LoadSystem()
                LoadStorage()
                CopyToTheOtherMode()
                WallpaperOptionsUI()
            }
        }
    }
}

/**
 * Loads system wallpaper and uses it as background
 * */
@Composable
private fun LoadSystem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = viewModelKt(modelClass = WallpaperViewModel::class)

    var dialogShown by rememberMutableSaveable {
        mutableStateOf(false)
    }

    Button(
        onClick = {
            @SuppressLint("MissingPermission")
            if (hasStoragePermissionForSystem(context)) {
                viewModel.handleSystemBackground()
            } else {
                dialogShown = true
            }
        },
        modifier = modifier,
    ) {
        Text(text = stringResource(id = R.string.ui_system_wallpaper))
    }

    RequestStoragePermissionDialog(shown = dialogShown, onShownChanged = { dialogShown = it })
}

/**
 * Show dialog to choose an image from storage
 * */
@Composable
private fun LoadStorage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = viewModelKt(modelClass = WallpaperViewModel::class)

    var dialogShown by rememberMutableSaveable {
        mutableStateOf(false)
    }

    //contract to open file picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            viewModel.handleUri(it)
        }
    }

    Button(
        onClick = {
            @SuppressLint("MissingPermission")
            if (hasStoragePermissionForFiles(context)) {

                //choose images only
                launcher.launch("image/*")

            } else {
                dialogShown = true
            }
        },
        modifier = modifier,
    ) {
        Text(text = stringResource(id = R.string.ui_choose_image))
    }

    RequestStoragePermissionDialog(shown = dialogShown, onShownChanged = { dialogShown = it })
}

/**
 * Copies current settings into the other mode (port/land)
 * */
@Composable
private fun CopyToTheOtherMode(modifier: Modifier = Modifier) {

    val viewModel = viewModelKt(modelClass = WallpaperViewModel::class)
    val isPortrait by viewModel.isPortrait.collectAsState()

    Button(
        onClick = { viewModel.copyWallpaperFromTheOtherMode() },
        modifier = modifier,
    ) {

        val text = stringResource(id = R.string.ui_copy_from_other_mode).format(
            if (!isPortrait)
                stringResource(id = R.string.ui_copy_from_other_mode_portrait)
            else
                stringResource(id = R.string.ui_copy_from_other_mode_landscape)
        )

        Text(text = text)
    }
}

/**
 * Wallpaper transform options UI - zoom, bias, rotate
 * */
@Composable
private fun WallpaperOptionsUI(modifier: Modifier = Modifier) {

    val viewModel = viewModelKt(modelClass = WallpaperViewModel::class)
    val options by viewModel.wallpaperOptions.collectAsState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        SliderTextField(
            text = stringResource(id = R.string.ui_wallpaper_zoom),
            value = options.zoom,
            onValueChanged = { viewModel.setWallpaperOptions(options.copy(zoom = it)) },
            range = 1f..4f,
            valueToText = { (round(it * 100) / 100).toString() },
            textToValue = { it.toFloat() },
            modifier = Modifier.fillMaxWidth(),
        )

        SliderTextField(
            text = stringResource(id = R.string.ui_wallpaper_vertical),
            value = options.verticalBias,
            onValueChanged = { viewModel.setWallpaperOptions(options.copy(verticalBias = it)) },
            modifier = Modifier.fillMaxWidth(),
        )

        SliderTextField(
            text = stringResource(id = R.string.ui_wallpaper_horizontal),
            value = options.horizontalBias,
            onValueChanged = { viewModel.setWallpaperOptions(options.copy(horizontalBias = it)) },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedButton(
            onClick = { viewModel.setWallpaperOptions(options.copy(rotate = options.rotate - 1)) },
        ) {
            IconTextRow(
                painter = rememberVectorPainter(Icons.Default.Refresh),
                text = stringResource(id = R.string.ui_rotate),
            )
        }
    }
}