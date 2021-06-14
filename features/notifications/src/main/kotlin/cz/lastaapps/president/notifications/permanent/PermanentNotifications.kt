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

package cz.lastaapps.president.notifications.permanent

import android.app.Notification
import android.content.Context
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.core.president.PresidentState
import cz.lastaapps.president.notifications.NotificationsCreator
import cz.lastaapps.president.notifications.R

/**
 * Creates notification for service
 * */
internal class PermanentNotifications(context: Context) : NotificationsCreator(context) {

    companion object {
        private const val pendingRequest = 43686
    }

    override val channelId: String
        get() = "permanent_notification_channel"
    override val channelName: Int
        get() = R.string.permanent_channel_name
    override val channelDescription: Int
        get() = R.string.permanent_channel_description

    //placeholder until data are loaded
    fun createInitial(): Notification {
        return createNotification(
            context.getString(R.string.permanent_loading_title),
            context.getString(R.string.permanent_loading_text),
            pendingRequest
        )
    }

    //placeholder when screen is off
    fun createPaused(): Notification {
        return createNotification(
            context.getString(R.string.permanent_paused_title),
            context.getString(R.string.permanent_paused_text),
            pendingRequest
        )
    }

    //main notification
    fun createForTime(state: CurrentState): Notification {

        when {
            //normal time left
            state.state.isTimeRemainingSupported -> {
                val title = context.getString(R.string.permanent_template_title)
                val text = state.run {
                    context.getString(
                        R.string.permanent_template_text,
                        years, days, hours, minutes, seconds,
                    )
                }

                return createNotification(title, text, pendingRequest)
            }
            //another placeholder
            state.state == PresidentState.LOADING -> {
                return createNotification("", "", pendingRequest)
            }
            //another state
            else -> {
                return createNotification(
                    context.getString(R.string.permanent_over_title),
                    context.getString(state.state.stringId),
                    pendingRequest
                )
            }
        }
    }
}