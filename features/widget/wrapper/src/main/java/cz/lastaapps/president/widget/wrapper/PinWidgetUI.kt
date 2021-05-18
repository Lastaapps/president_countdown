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

package cz.lastaapps.president.widget.wrapper

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cz.lastaapps.president.core.functionality.PendingIntentCompat

/**
 * Tries to pin a widget, or shows legacy help dialog
 * */
@Composable
fun PinWidgetDialog(
    shown: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (shown) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(color = MaterialTheme.colors.background) {
                Box(Modifier.padding(16.dp)) {

                    val context = LocalContext.current
                    val pinningEnabled = remember(context) { pinningEnabled(context) }

                    if (pinningEnabled) {
                        PinningDialog(onDismissRequest)
                    } else {
                        LegacyContent()
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
private fun PinningDialog(onDismissRequest: () -> Unit) {

    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            stringResource(id = R.string.widget_pinning_choice_title),
            style = MaterialTheme.typography.h4,
            maxLines = 1,
            modifier = Modifier
        )

        Column(
            Modifier.width(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            WidgetsCombiner.modules.forEach {
                Button(onClick = {
                    requestPinning(context, it)
                    onDismissRequest()
                }, Modifier.fillMaxWidth()) {
                    Text(stringResource(id = it.name))
                }
            }
        }
    }
}

@Composable
private fun LegacyContent() {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.widget_pinning_legacy_title),
            style = MaterialTheme.typography.h4,
        )
        Text(text = stringResource(id = R.string.widget_pinning_legacy_text))
        Button(
            onClick = {
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(startMain)
            }
        ) {
            Text(text = stringResource(id = R.string.widget_pinning_legacy_ok))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun requestPinning(context: Context, module: ModuleSpec) {
    val configIntent = Intent(context, module.configActivity.java)
    val pending = PendingIntent.getActivity(
        context,
        module.viewUpdater.pinningRequestCode,
        configIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_MUTABLE,
    )

    //shows a dialog to place the core directly
    //add callback for launching the configuration activity
    val mgr = AppWidgetManager.getInstance(context)
    mgr.requestPinAppWidget(
        ComponentName(context, module.widget.java),
        Bundle().apply {
            //putString(AppWidgetManager.EXTRA_APPWIDGET_PREVIEW, )
        },
        pending,
    )
}

private fun pinningEnabled(context: Context): Boolean {
    val mgr = AppWidgetManager.getInstance(context)
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mgr.isRequestPinAppWidgetSupported
}
