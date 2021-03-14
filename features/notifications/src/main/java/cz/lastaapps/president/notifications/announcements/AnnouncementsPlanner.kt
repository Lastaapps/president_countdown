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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import cz.lastaapps.president.core.functionality.CET
import cz.lastaapps.president.core.functionality.LocalDateCET
import cz.lastaapps.president.core.functionality.PendingIntentCompat
import cz.lastaapps.president.core.president.President
import cz.lastaapps.president.notifications.announcements.AnnouncementType.Companion.putAnnouncedTypeExtra
import cz.lastaapps.president.notifications.settings.Settings
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal class AnnouncementsPlanner(private val context: Context) {

    companion object {
        private val TAG get() = AnnouncementsPlanner::class.simpleName
    }

    /**
     * Plans all the events
     * */
    fun plan(settings: Settings) {

        //creates a notification channel
        AnnouncementsNotifications(context)

        //when in the day will be the notification posted (like at 8 am) in CET
        val time = settings.notificationsTimeFlow.value

        //enables on cancels events
        val enabled = settings.announcementsFlow.value

        Log.i(TAG, "Planning: $enabled")

        setDayAlarm(
            President.birthDate, time,
            AnnouncementType.BIRTHDAY,
            enabled
        )

        setDayAlarm(
            President.namesDate, time,
            AnnouncementType.NAMEDAY,
            enabled
        )

        setDayAlarm(
            President.elected, time,
            AnnouncementType.ELECTED,
            enabled
        )

        setDayAlarm(
            President.mandateEnd, time,
            AnnouncementType.REMAINS,
            enabled
        )
    }

    /**Sets pending intent into AlarmManager which updated data during day*/
    private fun setDayAlarm(
        date: LocalDate,
        time: LocalTime,
        @AnnouncementType type: Int,
        enabled: Boolean
    ) {

        val year = LocalDateCET.now().year
        var dateTime = ZonedDateTime.of(date.withYear(year), time, CET)

        val now = ZonedDateTime.now()
        //gets the next appearance of the event
        while (dateTime < now) {
            dateTime = dateTime.plusYears(1)
        }

        Log.d(TAG, "Planning $type for ${dateTime.format(DateTimeFormatter.ISO_DATE_TIME)}")

        //saves the event type into intent
        val intent = Intent(context, AnnouncementsReceiver::class.java)
            .putAnnouncedTypeExtra(type)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            type,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_IMMUTABLE
        )

        setAlarm(pendingIntent, dateTime, enabled)
    }

    /**Puts Pending intent into AlarmManager*/
    private fun setAlarm(pendingIntent: PendingIntent, time: ZonedDateTime, enabled: Boolean) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (enabled) {
            alarmManager.set(
                AlarmManager.RTC,
                time.toInstant().toEpochMilli(),
                pendingIntent,
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }
}