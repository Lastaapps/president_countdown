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

package cz.lastaapps.president.notifications.daily

import android.app.Notification
import android.content.Context
import cz.lastaapps.president.core.functionality.getQuantityString
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.notifications.NotificationsCreator
import cz.lastaapps.president.notifications.R

/**
 * Creates daily notification
 * */
internal class DailyNotifications(context: Context) : NotificationsCreator(context) {

    companion object {
        private const val pendingRequest = 49104
    }

    override val channelId: String
        get() = "daily_notification_channel"
    override val channelName: Int
        get() = R.string.daily_channel_name
    override val channelDescription: Int
        get() = R.string.daily_channel_description

    private val title get() = context.getString(R.string.daily_title)
    private val complexTemplate get() = context.getString(R.string.daily_text_template_complex)
    private val simpleTemplate get() = context.getString(R.string.daily_text_template_simple)
    private val endTemplate get() = context.getString(R.string.daily_text_template_end)

    fun createDailyNotification(currentState: CurrentState): Notification {

        return createNormalDailyNotification(
            currentState.years.toInt(),
            currentState.days.toInt(),
        )
        /*if (currentState.state.isTimeRemainingSupported) {
            return createNormalDailyNotification(
                currentState.years.toInt(),
                currentState.days.toInt()
            )
        } else {
            throw IllegalArgumentException("Cannot show notification for this state: ${currentState.state}")
        }*/
    }

    private fun createNormalDailyNotification(years: Int, days: Int): Notification {

        val yearsText = context.getQuantityString(R.plurals.daily_text_years, years, years)
        val daysText = context.getQuantityString(R.plurals.daily_text_days, days, days)

        //when there is 0 of something, we make it hidden
        val text = when ((years > 0) to (days > 0)) {
            true to true -> {
                complexTemplate.format(yearsText, daysText)
            }
            true to false -> {
                simpleTemplate.format(yearsText)
            }
            false to true -> {
                simpleTemplate.format(daysText)
            }
            else -> {
                if (years == 0 && days == 0) {
                    simpleTemplate.format(daysText)
                } else {
                    endTemplate
                }
            }
        }

        return createNotification(title, text, pendingRequest)
    }
}