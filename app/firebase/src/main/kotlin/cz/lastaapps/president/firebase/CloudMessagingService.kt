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

package cz.lastaapps.president.firebase

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

/**
 * Prints out message tokens and managers notification channels
 * */
class CloudMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = CloudMessagingService::class.simpleName

        private const val announcementsChannel = "firebase_announcements_channel"
        private const val developerNotes = "firebase_developer_notes"

        fun createChannels(context: Context) {
            val mgr = NotificationManagerCompat.from(context)

            val announcementChannel = NotificationChannelCompat.Builder(
                announcementsChannel,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .setName(context.getString(R.string.firebase_announcements_channel_name))
                .setDescription(context.getString(R.string.firebase_announcements_channel_description))
                .setShowBadge(true)
                .build()

            val developerChannel = NotificationChannelCompat.Builder(
                developerNotes,
                NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
                .setName(context.getString(R.string.firebase_developer_notes_name))
                .setDescription(context.getString(R.string.firebase_developer_notes_description))
                .setShowBadge(false)
                .setSound(null, null)
                .build()

            mgr.createNotificationChannel(announcementChannel)
            mgr.createNotificationChannel(developerChannel)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val refreshedToken = FirebaseMessaging.getInstance().token

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Refreshed token: $refreshedToken")
    }
}