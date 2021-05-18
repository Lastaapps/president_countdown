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

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Size
import android.view.View
import android.widget.RemoteViews
import androidx.core.text.HtmlCompat
import cz.lastaapps.president.core.functionality.PendingIntentCompat
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.core.president.TimePlurals
import cz.lastaapps.president.core.president.get
import cz.lastaapps.president.widget.core.R
import kotlin.reflect.KClass

/**
 * Manages all the work with remote view for the core
 * */
abstract class RemoteViewUpdater {

    protected abstract val pendingRequest: Int
    abstract val pinningRequestCode: Int
    protected abstract val configActivity: KClass<out Activity>

    abstract val preferredAspectRation: Float

    protected abstract val layoutId: Int

    /** values ids - the time numbers */
    protected abstract val values: List<Int?>

    /** units ids - the time units */
    protected abstract val units: List<Int?>
    protected abstract val borders: List<Int>
    protected abstract val rootId: Int
    protected abstract val stateId: Int
    protected abstract val timeId: Int
    protected abstract val backgroundId: Int
    protected abstract val yearId: Int?

    /**
     * Creates RemoteViews for the core
     * @param widgetId opens WidgetConfigActivity on core click
     * (if AppWidgetManager.INVALID_APPWIDGET_ID is used, clicks are disabled)
     * */
    fun createRemoteViews(context: Context, widgetId: Int): RemoteViews =
        RemoteViews(context.packageName, layoutId).also {

            val intent =
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
                //opens core configs
                    Intent(context, configActivity.java).also { intent ->
                        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    }
                else
                    return@also

            val pending = PendingIntent.getActivity(
                context,
                pendingRequest,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_IMMUTABLE
            )

            it.setOnClickPendingIntent(rootId, pending)
        }

    /**
     * Creates RemoteViews for the core
     * */
    fun createPlaceholder(context: Context): RemoteViews =
        RemoteViews(context.packageName, R.layout.placeholder)

    /**
     * Updates RemoteViews based on the state
     * */
    fun updateState(context: Context, views: RemoteViews, state: CurrentState): Boolean {

        return if (state.state.isTimeRemainingSupported) {

            views.setViewVisibility(stateId, View.GONE)
            views.setViewVisibility(timeId, View.VISIBLE)

            val plurals = TimePlurals(context)

            for (i in 0 until 5) {
                val value = state[i]

                values.getOrNull(i)?.let { views.setTextViewText(it, value.toString()) }
                units.getOrNull(i)
                    ?.let { views.setTextViewText(it, plurals.getByIndex(i, value.toInt())) }
            }

            true

        } else {

            views.setViewVisibility(stateId, View.VISIBLE)
            views.setViewVisibility(timeId, View.GONE)

            val text = HtmlCompat.fromHtml(
                context.getString(state.state.formattedStringId),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            views.setTextViewText(stateId, text)

            false
        }
    }

    /**
     * Changes foreground and background colors of remote views
     * */
    fun updateColors(
        views: RemoteViews,
        foreground: Int,
        background: Int,
        firstColor: Int?,
        frameEnabled: Boolean,
        size: Size?,
    ) {

        //foreground
        values.filterNotNull().forEach { id ->
            views.setTextColor(id, foreground)
        }
        units.filterNotNull().forEach { id ->
            views.setTextColor(id, foreground)
        }
        views.setTextColor(stateId, foreground)

        borders.forEach {
            if (frameEnabled) {
                views.setViewVisibility(it, View.VISIBLE)
                views.setInt(it, "setBackgroundColor", foreground)
            } else
                views.setViewVisibility(it, View.GONE)
        }

        firstColor?.let {
            yearId?.let { yearId ->
                views.setTextColor(yearId, it)
            }
        }

        //background
        views.setInt(backgroundId, "setBackgroundColor", background)

        size?.let {
            if (size.height < hideUnitLimit) {
                units.filterNotNull().forEach {
                    views.setViewVisibility(it, View.GONE)
                }
            }
        }
    }

    private val hideUnitLimit = 80
}