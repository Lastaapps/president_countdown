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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import cz.lastaapps.president.core.functionality.CET
import cz.lastaapps.president.core.functionality.PendingIntentCompat
import cz.lastaapps.president.notifications.ui.settings.NotificationSettingsRepo
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

/**
 * Plans daily notifications
 * */
internal class DailyPlanner(private val context: Context) {

    fun plan(settings: NotificationSettingsRepo) {

        //creates a notification channel
        DailyNotifications(context)

        val enabled = settings.dailyFlow.value
        val minutes = settings.notificationsTimeFlow.value

        setEverydaySetup(enabled, minutes)
    }

    /**Updates every morning and init new date*/
    private fun setEverydaySetup(enabled: Boolean, time: LocalTime) {

        val intent = Intent(context, DailyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DailyReceiver.REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_IMMUTABLE
        )

        var dateTime = ZonedDateTime.of(LocalDate.now(), time, CET)
        if (dateTime <= ZonedDateTime.now()) {
            dateTime = dateTime.plusDays(1)
        }

        setRepeatingAlarm(pendingIntent, dateTime, enabled)
    }

    /**Puts Pending intent into AlarmManager as repeating*/
    private fun setRepeatingAlarm(
        pendingIntent: PendingIntent,
        time: ZonedDateTime,
        enabled: Boolean
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (enabled) {
            alarmManager.setRepeating(
                AlarmManager.RTC,
                time.toInstant().toEpochMilli(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent,
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }
}