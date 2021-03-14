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

package cz.lastaapps.president.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.res.Configuration
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import cz.lastaapps.president.core.president.CurrentState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Updates widgets every second + shows notifications
 * */
class WidgetUpdateService : Service() {

    companion object {
        private val TAG = WidgetUpdateService::class.simpleName

        private const val NOTIFICATION_ID = 49250
        private const val NOTIFICATION_DEBUG_STATE_ID = 49251
        private const val NOTIFICATION_DEBUG_UPDATED_ID = 49252

        fun startService(context: Context) {

            //creates a notification channel
            Notifications(context)

            val intent = Intent(context, WidgetUpdateService::class.java)

            Log.i(TAG, "Requesting service foreground start")

            //starts service in foreground
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(intent)
            else
                context.startService(intent)
        }

        fun stopService(context: Context) {
            Log.i(TAG, "Requesting service stop")

            val intent = Intent(context, WidgetUpdateService::class.java)
            context.stopService(intent)
        }
    }

    private lateinit var notifications: Notifications
    private val mgr by lazy { NotificationManagerCompat.from(this) }

    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    private var debug = false

    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "Creating service, debug is: $debug")

        notifications = Notifications(this)

        startForeground(NOTIFICATION_ID, notifications.createBasicNotification())

        registerReceiver(screenOnReceiver, IntentFilter("android.intent.action.SCREEN_ON"))
        registerReceiver(screenOffReceiver, IntentFilter("android.intent.action.SCREEN_OFF"))

        startJob()

        scope.launch {
            Settings.debugFlow.collectLatest {
                debug = it
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Log.i(TAG, "Starting service")

        return START_STICKY
    }

    //updates the notification when the language changes
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mgr.notify(NOTIFICATION_ID, notifications.createBasicNotification())
    }

    private fun startJob() {
        Log.i(TAG, "Starting update job")

        job?.cancel()

        if (!checkWidgetsPresented()) {
            Log.e(TAG, "No widgets found, stopping")

            stopForeground(true)
            stopSelf()
            return
        }

        job = startUpdating()

        if (debug) {
            mgr.notify(
                NOTIFICATION_DEBUG_STATE_ID,
                notifications.createDebugStateNotification(true)
            )
        }
    }

    private fun stopJob() {
        Log.i(TAG, "Stopping update job")

        job?.cancel()
        job = null

        if (debug) {
            mgr.notify(
                NOTIFICATION_DEBUG_STATE_ID,
                notifications.createDebugStateNotification(false)
            )
        }
    }

    private fun startUpdating() = scope.launch {

        val context = this@WidgetUpdateService

        CurrentState.getCurrentBuffered(scope).collect {
            val views = RemoteViewUpdater.createRemoteViews(context)

            RemoteViewUpdater.updateViews(context, views, it)

            PresidentWidget.updateAllWithState(context, views)

            if (debug) {
                Log.d(TAG, "Updating ${LocalTime.now().format(DateTimeFormatter.ISO_TIME)}")

                NotificationManagerCompat.from(context).notify(
                    NOTIFICATION_DEBUG_UPDATED_ID,
                    notifications.createDebugUpdateNotification()
                )
            }

            delay(200)
        }
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

    private fun checkWidgetsPresented(): Boolean {
        val mgr = AppWidgetManager.getInstance(this)
        val widgets = mgr.getAppWidgetIds(
            ComponentName(this@WidgetUpdateService, PresidentWidget::class.java)
        )
        if (debug)
            Log.d(TAG, "There are ${widgets.size} widgets")

        return widgets.isNotEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(TAG, "Destroying")

        stopJob()
        scope.cancel()

        unregisterReceiver(screenOnReceiver)
        unregisterReceiver(screenOffReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}