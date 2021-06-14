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

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.notifications.ui.settings.NotificationSettingsRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 * Shows the current time left in a notification
 * */
class PermanentService : Service() {

    internal companion object {
        private val TAG = PermanentService::class.simpleName
        private const val notificationId = 41062

        private fun getServiceIntent(context: Context) =
            Intent(context, PermanentService::class.java)

        fun startService(context: Context, settings: NotificationSettingsRepo) {

            //creates a notification channel
            PermanentNotifications(context)

            if (settings.permanentFlow.value) {
                ContextCompat.startForegroundService(context, getServiceIntent(context))
            } else {
                stopService(context)
            }
        }

        fun stopService(context: Context) {
            context.stopService(getServiceIntent(context))
        }
    }

    private val notifications get() = PermanentNotifications(this)
    private val mgr by lazy { NotificationManagerCompat.from(this) }

    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "Creating service")

        startForeground(notificationId, notifications.createInitial())

        //update process in stopped when the screen is off
        registerReceiver(screenOnReceiver, IntentFilter("android.intent.action.SCREEN_ON"))
        registerReceiver(screenOffReceiver, IntentFilter("android.intent.action.SCREEN_OFF"))

        startJob()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(notificationId, notifications.createInitial())

        return START_NOT_STICKY
    }

    /**
     * The process updating the notification
     * */
    private fun updatingJob() = scope.launch {

        CurrentState.getCurrentBuffered(scope).collect {

            mgr.notify(notificationId, notifications.createForTime(it))

            //if there was a number skipped
            delay(200)
        }
    }

    /**
     * Start updating
     * */
    private fun startJob() {
        Log.i(TAG, "Starting job")
        mgr.notify(notificationId, notifications.createInitial())

        job?.cancel()
        job = updatingJob()
    }

    /**
     * Stop updating
     * */
    private fun stopJob() {
        Log.i(TAG, "Stopping the job")
        mgr.notify(notificationId, notifications.createPaused())

        job?.cancel()
        job = null
    }

    override fun onDestroy() {
        super.onDestroy()

        //stop updating job
        stopJob()
        scope.cancel()

        //unregister screen on/off receivers
        unregisterReceiver(screenOnReceiver)
        unregisterReceiver(screenOffReceiver)

        //dismiss notification
        stopForeground(true)
        mgr.cancel(notificationId)
    }

    private val screenOnReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            startJob()
        }
    }
    private val screenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            stopJob()
        }
    }

    override fun onBind(intent: Intent): IBinder? = null
}