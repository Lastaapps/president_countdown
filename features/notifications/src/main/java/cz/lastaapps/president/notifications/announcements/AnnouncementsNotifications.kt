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

package cz.lastaapps.president.notifications.announcements

import android.app.Notification
import android.content.Context
import cz.lastaapps.president.core.functionality.LocalDateCET
import cz.lastaapps.president.core.functionality.getLocale
import cz.lastaapps.president.core.functionality.getQuantityString
import cz.lastaapps.president.core.president.President
import cz.lastaapps.president.notifications.NotificationsCreator
import cz.lastaapps.president.notifications.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.absoluteValue

/**
 * Creates notifications for the specific events
 * */
internal class AnnouncementsNotifications(context: Context) : NotificationsCreator(context) {
    override val channelId: String get() = "announcements_notification_channel"
    override val channelName: Int get() = R.string.announcements_channel_name
    override val channelDescription: Int get() = R.string.announcements_channel_description
    private val pendingBirthdayRequest: Int get() = 14660
    private val pendingNamedayRequest: Int get() = 14661
    private val pendingElectedRequest: Int get() = 14662
    private val pendingRemainsRequest: Int get() = 14663

    private val nowCET get() = LocalDateCET.now()

    fun createBirthday(now: LocalDate = nowCET): Notification {

        val birthDate = President.birthDate

        val years = now.year - birthDate.year
        val pattern =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(context.getLocale())
        val birthDateText = birthDate.format(pattern)

        val title = context.getString(R.string.announcements_birthday_title, President.name)
        val text =
            context.getQuantityString(
                R.plurals.announcements_birthday_text,
                years,
                years,
                birthDateText
            )

        return createNotification(title, text, pendingBirthdayRequest)
    }

    fun createNameday(now: LocalDate = nowCET): Notification {
        val title = context.getString(R.string.announcements_nameday_title, President.name)
        val text = context.getString(R.string.announcements_nameday_text)

        return createNotification(title, text, pendingNamedayRequest)
    }

    fun createElected(now: LocalDate = nowCET): Notification {

        val elected = President.elected

        val years = now.year - elected.year

        val title = context.getString(R.string.announcements_elected_title, President.name)
        val text = context.getQuantityString(R.plurals.announcements_elected_text, years, years)

        return createNotification(title, text, pendingElectedRequest)
    }

    fun createRemains(now: LocalDate = nowCET): Notification {

        val mandateEnd = President.mandateEnd

        val years = mandateEnd.year - now.year
        val abs = years.absoluteValue

        val title: String
        val text: String

        when {
            years > 0 -> {
                title = context.getString(R.string.announcements_remains_title_future)
                text =
                    context.getQuantityString(R.plurals.announcements_remains_text_future, abs, abs)
            }
            years == 0 -> {
                title = context.getString(R.string.announcements_remains_title_now)
                text = context.getString(R.string.announcements_remains_text_now)
            }
            else -> {
                title =
                    context.getString(R.string.announcements_remains_title_history)
                text = context.getQuantityString(
                    R.plurals.announcements_remains_text_history,
                    abs,
                    abs
                )
            }
        }

        return createNotification(title, text, pendingRemainsRequest)
    }
}