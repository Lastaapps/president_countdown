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

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import cz.lastaapps.president.core.president.CurrentState

/**
 * Implementation of App Widget functionality.
 */
class PresidentWidget : AppWidgetProvider() {

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Log.v(TAG, "Enabling widgets")
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.v(TAG, "Disabling widgets")

        WidgetUpdateService.stopService(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.v(TAG, "Updating widgets")

        //TODO Not enabled in Android 12
        WidgetUpdateService.startService(context)

        val views = RemoteViewUpdater.createRemoteViews(context)
        RemoteViewUpdater.updateViews(context, views, CurrentState.getCurrentStateOnce())

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    companion object {
        private val TAG = PresidentWidget::class.simpleName

        /**
         * Uses these RemoteViews to update all the widgets
         * */
        internal fun updateAllWithState(context: Context, views: RemoteViews) {
            try {

                /*val update = Intent(context, PresidentWidget::class.java)
                update.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                context.sendBroadcast(update)
                */

                val appWidgetManager = AppWidgetManager.getInstance(context)
                appWidgetManager.updateAppWidget(
                    ComponentName(
                        context,
                        PresidentWidget::class.java
                    ), views
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}