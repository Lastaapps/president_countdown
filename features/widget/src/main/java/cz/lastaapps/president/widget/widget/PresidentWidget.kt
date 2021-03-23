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

package cz.lastaapps.president.widget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import cz.lastaapps.president.core.App
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.config.WidgetState
import cz.lastaapps.president.widget.config.database.WidgetDatabase
import cz.lastaapps.president.widget.config.getById
import cz.lastaapps.president.widget.config.updateColors
import cz.lastaapps.president.widget.service.WidgetUpdateService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Implementation of App Widget functionality.
 */
internal class PresidentWidget : AppWidgetProvider() {

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

        //creates a transparent placeholder
        val views = RemoteViewUpdater.createPlaceholder(context)
        appWidgetManager.updateAppWidget(appWidgetIds, views)

        GlobalScope.launch {
            delay(2000)
            WidgetDatabase.createDatabase(App.context).configRepo.updateWidgets(App.context)
        }
        //loads actual content
        //waits until service updates the widgets instead
        /*GlobalScope.launch {
            updateWithState(
                App.context,
                appWidgetIds,
                CurrentState.getCurrentState(this).value,
                WidgetDatabase.createDatabase(App.context).configDao().getByIds(appWidgetIds).first(),
            )
        }*/
    }

    companion object {
        private val TAG = PresidentWidget::class.simpleName

        /**
         * Updates all widgets corresponding to the state given
         * */
        internal fun updateAllWithState(
            context: Context,
            state: CurrentState,
            themes: List<WidgetState>
        ) {
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(ComponentName(context, PresidentWidget::class.java))

            updateWithState(context, ids, state, themes)
        }

        /**
         * Updates the widgets given corresponding to the state given
         * */
        internal fun updateWithState(
            context: Context,
            ids: IntArray,
            state: CurrentState,
            themes: List<WidgetState>
        ) {
            try {
                val mgr = AppWidgetManager.getInstance(context)

                val baseViews = RemoteViewUpdater.createRemoteViews(context)
                RemoteViewUpdater.updateState(context, baseViews, state)

                for (id in ids) {

                    val theme = themes.getById(id)

                    val themed = if (theme != null) {
                        @Suppress("DEPRECATION")
                        val views = if (Build.VERSION.SDK_INT >= 28)
                            RemoteViews(baseViews) else baseViews.clone()

                        RemoteViewUpdater.updateColors(context, views, theme)

                        views
                    } else {
                        Log.e(TAG, "Skipping widget $id! No theme found.")
                        baseViews
                    }

                    mgr.updateAppWidget(id, themed)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}