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

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModelProvider
import cz.lastaapps.president.wallpaper.settings.WallpaperViewModel
import cz.lastaapps.ui.common.extencions.centerToWithPadding
import cz.lastaapps.ui.common.themes.MainTheme

private const val TAG = "WallpaperSettingsUI"

@Composable
fun WallpaperSettings(modifier: Modifier = Modifier) {

    val sideMargins = 8.dp
    val viewModel = viewModel()
    val isDay by viewModel.isDayPreview.collectAsState()

    val config = LocalConfiguration.current
    remember(config) {
        viewModel.setIsLand(config.orientation % 2 == 1)
        viewModel.setIsDay(config.uiMode and Configuration.UI_MODE_NIGHT_YES == 0)
    }

    MainTheme(lightTheme = isDay) {
        Surface(color = MaterialTheme.colors.background) {

            ConstraintLayout(modifier = modifier.fillMaxSize()) {

                val (settingsConst, wallpaperConst) = createRefs()

                WallpaperCanvas(
                    Modifier
                        .constrainAs(wallpaperConst) {
                            centerTo(parent)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                )

                WallpaperOptionsLayout(
                    Modifier
                        .constrainAs(settingsConst) {
                            centerToWithPadding(parent, sideMargins)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                )
            }
        }
    }
}

@Composable
private fun WallpaperCanvas(modifier: Modifier = Modifier) {

    val viewModel = viewModel()
    val scope = rememberCoroutineScope()
    val flow by remember { mutableStateOf(viewModel.subscribeForBitmap(scope)) }
    val bitmap by flow.collectAsState()

    Image(
        bitmap = bitmap,
        null,
        modifier = modifier
            .onSizeChanged {
                viewModel.setCanvasSize(
                    it
                        .toSize()
                        .toRect()
                        .toAndroidRect()
                )
            }
    )
}

@Composable
internal fun viewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory? = null
): WallpaperViewModel =
    cz.lastaapps.ui.common.extencions.viewModelKt(WallpaperViewModel::class, key, factory)
