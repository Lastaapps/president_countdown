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

package cz.lastaapps.president.app.ui.main

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import cz.lastaapps.president.app.R
import cz.lastaapps.president.navigation.NavigationConstants
import cz.lastaapps.president.wallpaper.service.PresidentWallpaperService
import cz.lastaapps.president.widget.WidgetConfig
import cz.lastaapps.ui.common.components.ClickableButton
import cz.lastaapps.ui.common.components.IconTextRow
import cz.lastaapps.ui.common.extencions.iconSize
import cz.lastaapps.ui.socials.DeveloperNotice

/**
 * Navigation for the Main content
 * */
@Composable
internal fun Overview(
    navController: NavController,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        val paddingModifier = Modifier.padding(4.dp)

        //Navigation through the app
        Wallpaper(navController, paddingModifier)
        Widget(paddingModifier)
        Notifications(navController, paddingModifier)
        About(navController, paddingModifier)

        DeveloperNotice(paddingModifier)
    }
}

@Composable
private fun Wallpaper(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = { wallpaperAction(context) },
        ) {
            IconTextRow(
                Icons.Default.Wallpaper,
                text = stringResource(R.string.wallpaper),
                iconSize = iconSize
            )
        }
        IconButton(
            onClick = { navController.navigate(NavigationConstants.id.wallpaperSettings) },
        ) {
            Icon(Icons.Default.Settings, contentDescription = null)
        }
    }
}

@Composable
private fun Widget(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = mainViewModel()

    //used to enable the onLongClick functionality
    ClickableButton(
        onClick = { widgetAction(context) },
        onLongClick = { viewModel.toggleWidgetDebugging(context) },
        modifier = modifier
    ) {
        IconTextRow(
            Icons.Default.Widgets,
            text = stringResource(id = R.string.widget),
            iconSize = iconSize
        )
    }
}

@Composable
private fun Notifications(navController: NavController, modifier: Modifier = Modifier) {

    Button(
        onClick = {
            navController.navigate(NavigationConstants.id.notificationSettings)
        },
        modifier = modifier
    ) {
        IconTextRow(
            Icons.Default.Notifications,
            text = stringResource(id = R.string.notification),
            iconSize = iconSize,
        )
    }
}

@Composable
private fun About(navController: NavController, modifier: Modifier = Modifier) {

    Button(
        onClick = { navController.navigate(NavigationConstants.id.about) },
        modifier = modifier
    ) {
        IconTextRow(
            Icons.Default.Info,
            text = stringResource(id = R.string.about),
            iconSize = iconSize
        )
    }
}

private fun widgetAction(context: Context) {

    if (!WidgetConfig.requestWidgetPinning(context)) {
        //show a dialog with help how to place a widget on the home screen
        AlertDialog.Builder(context)
            .setMessage(R.string.widget_help)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(startMain)

                dialog.dismiss()
            }
            .create()
            .show()
    }
}

private fun wallpaperAction(context: Context) {
    val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
    intent.putExtra(
        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
        ComponentName(context, PresidentWallpaperService::class.java)
    )
    context.startActivity(intent)
}
