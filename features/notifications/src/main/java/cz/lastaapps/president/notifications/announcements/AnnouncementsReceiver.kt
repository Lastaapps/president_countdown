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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import cz.lastaapps.president.notifications.announcements.AnnouncementType.Companion.getAnnouncedTypeExtra
import cz.lastaapps.president.notifications.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * Show notification when retrieved from AlarmManager
 * */
internal class AnnouncementsReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = AnnouncementsReceiver::class.simpleName

        private const val BIRTHDAY_NOTIFICATION_ID = 53360
        private const val NAMEDAY_NOTIFICATION_ID = 53361
        private const val ELECTED_NOTIFICATION_ID = 53362
        private const val REMAINS_NOTIFICATION_ID = 53363
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Intent received")

        val mgr = NotificationManagerCompat.from(context)

        val type = intent.getAnnouncedTypeExtra()
        val notify = AnnouncementsNotifications(context)

        when (type) {
            AnnouncementType.BIRTHDAY -> {
                mgr.notify(BIRTHDAY_NOTIFICATION_ID, notify.createBirthday())
            }
            AnnouncementType.NAMEDAY -> {
                mgr.notify(NAMEDAY_NOTIFICATION_ID, notify.createNameday())
            }
            AnnouncementType.ELECTED -> {
                mgr.notify(ELECTED_NOTIFICATION_ID, notify.createElected())
            }
            AnnouncementType.REMAINS -> {
                mgr.notify(REMAINS_NOTIFICATION_ID, notify.createRemains())
            }
            else -> {
                Log.e(TAG, "Unknown type $type")
            }
        }

        //reschedules the events
        val scope = CoroutineScope(Dispatchers.Default)
        val settings = runBlocking(Dispatchers.Default) {
            Settings.getInstance(context, scope)
        }

        AnnouncementsPlanner(context).plan(settings)
    }
}