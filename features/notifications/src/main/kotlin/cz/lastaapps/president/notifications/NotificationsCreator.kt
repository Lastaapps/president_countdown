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

package cz.lastaapps.president.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cz.lastaapps.president.core.functionality.PendingIntentCompat

/**
 * Contains common methods for creating notifications
 * */
internal abstract class NotificationsCreator(protected val context: Context) {

    protected abstract val channelId: String
    protected abstract val channelName: Int
    protected abstract val channelDescription: Int


    init {
        //creates notification channel
        val channel = NotificationChannelCompat.Builder(
            channelId, NotificationManagerCompat.IMPORTANCE_LOW
        ).apply {
            setName(context.getString(channelName))
            setDescription(context.getString(channelDescription))
            setShowBadge(false)
            setLightsEnabled(false)
            setSound(null, null)
            setVibrationEnabled(false)
        }.build()

        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    /**
     * @return basic notification with data given
     * */
    protected fun createNotification(
        title: String,
        text: String,
        pendingRequest: Int
    ): Notification {
        return NotificationCompat.Builder(context, channelId).apply {
            setContentTitle(title)
            setContentText(text)
            setSmallIcon(cz.lastaapps.president.assets.R.drawable.flag_icon_outlined)
            setAutoCancel(true)
            setShowWhen(false)
            setSilent(true)
            //TODO setShowForegroundImmediately Android 12

            //opened activity if there is one set using NotificationConfig
            NotificationsConfig.mainActivity?.let { activity ->
                val intent = Intent(context, activity.java)
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    pendingRequest,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_IMMUTABLE
                )

                setContentIntent(pendingIntent)
            }
        }.build()
    }

}