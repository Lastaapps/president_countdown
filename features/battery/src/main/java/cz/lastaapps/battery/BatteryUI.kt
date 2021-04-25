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

package cz.lastaapps.battery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.lastaapps.president.core.coroutines.loopingStateFlow
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.themes.extended
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


/**
 * Informs user about background restrictions risks and gives him options to change it
 * */
@Composable
internal fun BatteryOptimizationDialog(shown: Boolean, onShownChanged: (Boolean) -> Unit) {
    if (shown) {
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = { onShownChanged(false) },
            title = {
                Text(text = stringResource(id = R.string.battery_title))
            },
            text = {
                Text(text = stringResource(id = R.string.battery_text))
            },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                ) {
                    TextButton(
                        onClick = { onShownChanged(false) },
                    ) {
                        Text(text = stringResource(id = R.string.battery_ignore))
                    }
                    TextButton(
                        onClick = { showBatterySettings(context) },
                    ) {
                        Text(text = stringResource(id = R.string.battery_settings))
                    }
                    Button(
                        onClick = { showAppInfo(context) },
                    ) {
                        Text(text = stringResource(id = R.string.battery_open))
                    }
                }
            },
        )
    }
}

/**
 * Blinking triangle indicating background usage is disabled
 * */
@Composable
fun BatteryWarning(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    //if background usage is disabled and updates every second
    val enabled by remember(context) {
        loopingStateFlow(scope) {
            shouldShowBatteryOptimizationDialog(context)
        }
    }.collectAsState()

    var dialogShown by rememberMutableSaveable {
        mutableStateOf(enabled)
    }

    if (enabled) {

        val warning = MaterialTheme.extended.waring
        val iconColor by remember(warning) {
            val blinkPeriod = 1500L
            flow {
                while (true) {
                    emit(warning)
                    delay(blinkPeriod / 2)
                    emit(Color.Transparent)
                    delay(blinkPeriod / 2)
                }
            }
        }.collectAsState(warning)

        if (!dialogShown) {
            IconButton(
                onClick = { dialogShown = !dialogShown },
                modifier = modifier,
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = stringResource(id = R.string.battery_warning_content_description),
                    tint = iconColor,
                )
            }
        }
    }

    BatteryOptimizationDialog(
        shown = dialogShown,
        onShownChanged = { dialogShown = it },
    )
}