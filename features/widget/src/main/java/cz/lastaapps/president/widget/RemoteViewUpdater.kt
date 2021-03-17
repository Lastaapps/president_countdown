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
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.core.text.HtmlCompat
import cz.lastaapps.president.clock.TimePlurals
import cz.lastaapps.president.core.functionality.PendingIntentCompat
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.core.president.get

/**
 * Manages all the work with remote view for the widget
 * */
internal object RemoteViewUpdater {

    private const val pendingRequest = 48134

    /**
     * Creates RemoteViews for the widget
     * */
    internal fun createRemoteViews(context: Context): RemoteViews =
        RemoteViews(context.packageName, R.layout.widget).also {

            val intent = Intent(context, WidgetClickActivity::class.java)
            val pending = PendingIntent.getActivity(
                context,
                pendingRequest,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_IMMUTABLE
            )

            it.setOnClickPendingIntent(R.id.root, pending)
        }

    /**
     * Updates RemoteViews based on the state
     * */
    internal fun updateViews(context: Context, views: RemoteViews, state: CurrentState): Boolean {

        return if (state.state.isTimeRemainingSupported) {

            views.setViewVisibility(R.id.state, View.GONE)
            views.setViewVisibility(R.id.time, View.VISIBLE)

            val values = listOf(R.id.yv, R.id.dv, R.id.hv, R.id.mv, R.id.sv)
            val units = listOf(R.id.yu, R.id.du, R.id.hu, R.id.mu, R.id.su)

            val plurals = TimePlurals(context)

            for (i in 0 until 5) {
                val value = state[i]

                val v1 = plurals.getByIndex(i, value.toInt())

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
}