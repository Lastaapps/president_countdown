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

package cz.lastaapps.president.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import cz.lastaapps.president.core.functionality.PendingIntentCompat
import cz.lastaapps.president.widget.config.WidgetConfigActivity
import cz.lastaapps.president.widget.service.WidgetUpdateService
import cz.lastaapps.president.widget.widget.DebugSettings
import cz.lastaapps.president.widget.widget.PresidentWidget

/**
 * Entry point for the module
 * */
object WidgetConfig {

    /**
     * Tries to pin a widget
     * @return if app pinning is available and pinning request has been made
     * */
    fun requestWidgetPinning(context: Context): Boolean {

        val mgr = AppWidgetManager.getInstance(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mgr.isRequestPinAppWidgetSupported) {

            val configIntent = Intent(context, WidgetConfigActivity::class.java)
            val pending = PendingIntent.getActivity(
                context,
                WidgetConfigActivity.REQUEST_CODE,
                configIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_MUTABLE,
            )

            //shows a dialog to place the widget directly
            //add callback for launching the configuration activity
            mgr.requestPinAppWidget(
                ComponentName(context, PresidentWidget::class.java),
                Bundle().apply {
                    //putString(AppWidgetManager.EXTRA_APPWIDGET_PREVIEW, )
                },
                pending,
            )

            true

        } else false
    }

    fun updateService(context: Context) {
        WidgetUpdateService.startService(context)
    }

    /**Toggles widget debug mode
     * @return new state*/
    fun toggleDebug(): Boolean {
        return (!DebugSettings.debugFlow.value).also {
            DebugSettings.debug = it
        }
    }
}