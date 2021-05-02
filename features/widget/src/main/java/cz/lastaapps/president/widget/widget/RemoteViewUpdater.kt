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

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Size
import android.view.View
import android.widget.RemoteViews
import androidx.core.text.HtmlCompat
import cz.lastaapps.president.core.App
import cz.lastaapps.president.core.functionality.PendingIntentCompat
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.core.president.TimePlurals
import cz.lastaapps.president.core.president.get
import cz.lastaapps.president.widget.R
import cz.lastaapps.president.widget.config.WidgetConfigActivity

/**
 * Manages all the work with remote view for the widget
 * */
internal object RemoteViewUpdater {

    private const val pendingRequest = 48130

    /** values ids - the time numbers */
    private val values = listOf(R.id.yv, R.id.dv, R.id.hv, R.id.mv, R.id.sv)

    /** units ids - the time units */
    private val units = listOf(R.id.yu, R.id.du, R.id.hu, R.id.mu, R.id.su)

    /**
     * Creates RemoteViews for the widget
     * @param widgetId opens WidgetConfigActivity on widget click
     * (if AppWidgetManager.INVALID_APPWIDGET_ID is used, clicks are disabled)
     * */
    fun createRemoteViews(context: Context, widgetId: Int): RemoteViews =
        RemoteViews(context.packageName, R.layout.widget).also {

            val intent =
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
                //opens widget configs
                    Intent(context, WidgetConfigActivity::class.java).also { intent ->
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

            it.setOnClickPendingIntent(R.id.root, pending)
        }

    /**
     * Creates RemoteViews for the widget
     * */
    fun createPlaceholder(context: Context): RemoteViews =
        RemoteViews(context.packageName, R.layout.placeholder)

    /**
     * Updates RemoteViews based on the state
     * */
    fun updateState(context: Context, views: RemoteViews, state: CurrentState): Boolean {

        return if (state.state.isTimeRemainingSupported) {

            views.setViewVisibility(R.id.state, View.GONE)
            views.setViewVisibility(R.id.time, View.VISIBLE)

            val plurals = TimePlurals(context)

            for (i in 0 until 5) {
                val value = state[i]

                views.setTextViewText(values[i], value.toString())
                views.setTextViewText(units[i], plurals.getByIndex(i, value.toInt()))
            }

            true

        } else {

            views.setViewVisibility(R.id.state, View.VISIBLE)
            views.setViewVisibility(R.id.time, View.GONE)

            val text = HtmlCompat.fromHtml(
                context.getString(state.state.formattedStringId),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            views.setTextViewText(R.id.state, text)

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
        values.forEach { id ->
            views.setTextColor(id, foreground)
        }
        units.forEach { id ->
            views.setTextColor(id, foreground)
        }
        views.setTextColor(R.id.state, foreground)

        listOf(R.id.border_top, R.id.border_bottom, R.id.border_start, R.id.border_end).forEach {
            if (frameEnabled) {
                views.setViewVisibility(it, View.VISIBLE)
                views.setInt(it, "setBackgroundColor", foreground)
            } else
                views.setViewVisibility(it, View.GONE)
        }

        firstColor?.let {
            views.setTextColor(R.id.yv, it)
        }

        //background
        views.setInt(R.id.background, "setBackgroundColor", background)

        size?.let {
            if (size.height < hideUnitLimit) {
                units.forEach {
                    views.setViewVisibility(it, View.GONE)
                }
            }
        }
    }

    private val hideUnitLimit by lazy { App.context.resources.getDimensionPixelSize(R.dimen.hide_unit_limit) }
}