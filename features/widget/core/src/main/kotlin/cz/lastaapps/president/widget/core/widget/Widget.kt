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

package cz.lastaapps.president.widget.core.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.util.Log
import android.util.Size
import cz.lastaapps.president.core.App
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.core.config.WidgetState
import cz.lastaapps.president.widget.core.config.database.WidgetDatabase
import cz.lastaapps.president.widget.core.config.getById
import cz.lastaapps.president.widget.core.config.updateColors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Implementation of App Widget functionality.
 */
abstract class Widget : AppWidgetProvider() {

    @Suppress("PropertyName")
    abstract val TAG: String

    abstract val viewUpdater: RemoteViewUpdater
    abstract fun getComponentName(context: Context): ComponentName

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first core is created
        Log.v(TAG, "Enabling widgets")
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last core is disabled
        Log.v(TAG, "Disabling widgets")
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.v(TAG, "Updating widgets")

        //creates a transparent placeholder
        val views = viewUpdater.createPlaceholder(context)
        appWidgetManager.updateAppWidget(appWidgetIds, views)

        //makes sure widgets get updated
        GlobalScope.launch {
            WidgetDatabase.createDatabase(App.context).configRepo.updateWidgets(
                App.context,
                allComponents,
            )
        }
    }

    companion object {

        lateinit var allComponents: List<ComponentName>

        /**
         * Updates all widgets corresponding to the state given
         * */
        fun updateAllWithState(
            context: Context,
            viewUpdater: RemoteViewUpdater,
            state: CurrentState,
            themes: List<WidgetState>,
            componentName: ComponentName,
        ) {
            val mgr = AppWidgetManager.getInstance(context)
            val ids = mgr.getAppWidgetIds(componentName)

            updateWithState(context, viewUpdater, ids, state, themes)
        }

        /**
         * Updates the widgets given corresponding to the state given
         * */
        fun updateWithState(
            context: Context,
            viewUpdater: RemoteViewUpdater,
            ids: IntArray,
            state: CurrentState,
            themes: List<WidgetState>,
        ) {
            try {
                val mgr = AppWidgetManager.getInstance(context)
                val defaultTheme by lazy { WidgetState.createDefault(AppWidgetManager.INVALID_APPWIDGET_ID) }

                for (widgetId in ids) {

                    val widgetViews = viewUpdater.createRemoteViews(context, widgetId)
                    viewUpdater.updateState(context, widgetViews, state)

                    val theme = themes.getById(widgetId) ?: defaultTheme

                    viewUpdater.updateColors(
                        context,
                        widgetViews,
                        theme,
                        mgr.getWidgetSize(widgetId),
                    )

                    mgr.updateAppWidget(widgetId, widgetViews)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

interface SimpleUpdate {

    /**
     * Updates all widgets corresponding to the state given
     * */
    fun updateAllWithState(
        context: Context,
        state: CurrentState,
        themes: List<WidgetState>
    )

    /**
     * Updates the widgets given corresponding to the state given
     * */
    fun updateWithState(
        context: Context,
        ids: IntArray,
        state: CurrentState,
        themes: List<WidgetState>
    )
}

private fun AppWidgetManager.getWidgetSize(widgetId: Int): Size =
    getAppWidgetOptions(widgetId).run {
        Size(
            getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH),
            getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT),
        )
    }