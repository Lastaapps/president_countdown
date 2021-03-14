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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.core.president.PresidentStateStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * Shows day notification after request for AlarmManager
 * */
internal class DailyReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = DailyReceiver::class.simpleName
        internal const val REQUEST_CODE = 30364
        private const val NOTIFICATION_ID = 18158
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Intent received")

        val mgr = NotificationManagerCompat.from(context)

        val state = runBlocking(Dispatchers.Default) { PresidentStateStorage.getCurrentState() }

        val currentState = CurrentState.createCurrentState(state)

        if (currentState.state.isTimeRemainingSupported)
            mgr.notify(
                NOTIFICATION_ID,
                DailyNotifications(context).createDailyNotification(currentState)
            )
        else
            Log.i(TAG, "No notification shows, unsupported president state")
    }
}