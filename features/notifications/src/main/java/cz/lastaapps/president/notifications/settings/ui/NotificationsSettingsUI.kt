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

package cz.lastaapps.president.notifications.settings.ui

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.lastaapps.president.clock.ClockLayout
import cz.lastaapps.president.notifications.R
import cz.lastaapps.ui.settings.SettingsGroup
import cz.lastaapps.ui.settings.SettingsGroupColumn
import cz.lastaapps.ui.settings.SwitchSettings
import cz.lastaapps.ui.settings.TimeSettings


@Composable
fun NotificationSettings(modifier: Modifier = Modifier) {

    ClockLayout(modifier) {

        val settingsInitialized = loadSettings()

        if (settingsInitialized) {
            Box(
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column(Modifier.padding(16.dp)) {

                    val fillWidth = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .fillMaxWidth()

                    SettingsGroup(modifier = fillWidth) {
                        NotificationTimeChooser(fillWidth)
                    }

                    SettingsGroupColumn(modifier = fillWidth) {
                        Announcement(fillWidth)
                        Daily(fillWidth)
                        Permanent(fillWidth)
                    }

                    OpenSystemSettings(fillWidth)
                }
            }
        } else {
            Box {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun loadSettings(): Boolean {

    var initialized by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel = viewModel()

    LaunchedEffect(context) {
        viewModel.initializeSettings()
        initialized = true
    }

    // Return the state-backed image property
    return initialized
}

@Composable
private fun NotificationTimeChooser(modifier: Modifier = Modifier) {

    val viewModel = viewModel()

    val state by remember { viewModel.notificationsTimeFlow }.collectAsState()

    TimeSettings(
        text = stringResource(id = R.string.sett_time),
        time = state,
        onTimeChanged = { viewModel.notificationsTime = it },
        modifier = modifier,
        timezone = stringResource(id = R.string.sett_time_CET_timezone)
    )
}

@Composable
private fun Announcement(modifier: Modifier = Modifier) {

    val viewModel = viewModel()

    val state = viewModel.announcementsFlow.collectAsState()

    SwitchSettings(
        text = stringResource(id = R.string.sett_announcements),
        checked = state.value,
        onChange = { viewModel.announcements = !state.value },
        modifier = modifier,
    )
}

@Composable
private fun Daily(modifier: Modifier = Modifier) {

    val viewModel = viewModel()

    val state = viewModel.dailyFlow.collectAsState()

    SwitchSettings(
        text = stringResource(id = R.string.sett_daily),
        checked = state.value,
        onChange = { viewModel.daily = !state.value },
        modifier = modifier,
    )
}

@Composable
private fun Permanent(modifier: Modifier = Modifier) {

    val viewModel = viewModel()

    val state = viewModel.permanentFlow.collectAsState()

    SwitchSettings(
        text = stringResource(id = R.string.sett_permanent),
        checked = state.value,
        onChange = { viewModel.permanent = !state.value },
        modifier = modifier,
    )
}

@Composable
private fun OpenSystemSettings(modifier: Modifier = Modifier) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val context = LocalContext.current

        Row(
            modifier = modifier
                .padding(8.dp)
                .clickable {
                    val intent: Intent =
                        Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(
                                android.provider.Settings.EXTRA_APP_PACKAGE,
                                context.packageName
                            )
                    context.startActivity(intent)
                },
            Arrangement.Center,
            Alignment.CenterVertically,
        ) {

            val text = stringResource(id = R.string.sett_system)

            Icon(Icons.Default.OpenInNew, contentDescription = text)
            Text(text = text)
        }
    }
}

@Composable
private fun viewModel() =
    cz.lastaapps.ui.common.extencions.viewModel(NotifyViewModel::class)

