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

package cz.lastaapps.president.widget.wrapper.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.lastaapps.president.core.functionality.PendingIntentCompat
import cz.lastaapps.president.widget.wrapper.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Creates notification for core (including debug)
 * */
internal class Notifications(private val context: Context) {

    private val channelId = "PRESIDENT_COUNTDOWN_WIDGET_CHANNEL"
    private val pendingRequest = 54139

    init {
        createChannel()
    }

    /**
     * Creates notification channel
     * */
    private fun createChannel() {
        val channel = NotificationChannelCompat.Builder(
            channelId,
            NotificationManagerCompat.IMPORTANCE_MIN
        )
            .setName(context.getString(R.string.notification_channel_name))
            .setDescription(context.getString(R.string.notification_channel_description))
            .setLightsEnabled(false)
            .setShowBadge(false)
            .setVibrationEnabled(false)
            .setSound(null, null)
            .build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    /**
     * Notification informing the user, that it is required (for a foreground service)
     * */
    fun createBasicNotification(): Notification {

        val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.getString(R.string.notification_content_text)
        else
            ""

        return createNotification(
            context.getString(R.string.notification_content_title),
            text,
        )
    }

    /**
     * Service started - stopped
     * */
    fun createDebugStateNotification(isStarted: Boolean): Notification {
        return createNotification("State updated", if (isStarted) "Started" else "Stopped")
    }

    /**
     * Shows current time
     * */
    fun createDebugUpdateNotification(time: LocalTime = LocalTime.now()): Notification {
        val content = time.format(DateTimeFormatter.ISO_TIME)

        return createNotification("Time updated", content)
    }

    private fun createNotification(title: String, text: String): Notification {
        return NotificationCompat.Builder(context, channelId).apply {
            setContentTitle(title)
            setContentText(text)
            setSmallIcon(cz.lastaapps.president.assets.R.drawable.flag_icon)
            setLocalOnly(true)
            setShowWhen(false)
            setSound(null)
            //TODO setShowForegroundImmediately in Android 12

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val intent: Intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
                val pending =
                    PendingIntent.getActivity(
                        context,
                        pendingRequest,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_IMMUTABLE
                    )

                setContentIntent(pending)
            }
        }.build()
    }
}