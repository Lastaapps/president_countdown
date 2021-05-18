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

package cz.lastaapps.president.notifications.ui.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.lastaapps.president.core.coroutines.mapState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import java.time.LocalTime

internal class NotificationSettingsRepo private constructor(
    context: Context,
) {
    private val scope: CoroutineScope = GlobalScope

    companion object {

        suspend fun getInstance(context: Context): NotificationSettingsRepo {
            return NotificationSettingsRepo(context).apply {
                initialize()
            }
        }

        private const val STORE_NAME = "notifications_settings"
        private val Context.dataStore by preferencesDataStore(STORE_NAME)
        private val KEY_ANNOUNCEMENTS = booleanPreferencesKey("announcements")
        private val KEY_DAILY = booleanPreferencesKey("daily")
        private val KEY_PERMANENT = booleanPreferencesKey("permanent")
        private val KEY_NOTIFICATIONS_TIME = intPreferencesKey("notifications_time")
    }

    private val dataStore = context.dataStore

    private suspend fun initialize() {
        announcementsFlow = mapDataStore(KEY_ANNOUNCEMENTS, true)
        dailyFlow = mapDataStore(KEY_DAILY, false)
        permanentFlow = mapDataStore(KEY_PERMANENT, false)
        notificationsTimeMinutesFlow = mapDataStore(KEY_NOTIFICATIONS_TIME, 8 * 60)
    }

    private suspend fun <T> mapDataStore(key: Preferences.Key<T>, default: T): StateFlow<T> =
        dataStore.data.map { it[key] ?: default }.distinctUntilChanged().stateIn(scope)

    private fun <T> update(key: Preferences.Key<T>, value: T) {
        runBlocking(Dispatchers.Default) {
            dataStore.edit {
                it[key] = value
            }
        }
    }

    lateinit var announcementsFlow: StateFlow<Boolean>
    var announcements: Boolean
        get() = announcementsFlow.value
        set(value) = update(KEY_ANNOUNCEMENTS, value)

    lateinit var dailyFlow: StateFlow<Boolean>
    var daily: Boolean
        get() = dailyFlow.value
        set(value) = update(KEY_DAILY, value)

    lateinit var permanentFlow: StateFlow<Boolean>
    var permanent: Boolean
        get() = permanentFlow.value
        set(value) = update(KEY_PERMANENT, value)

    //minutes in the day
    private lateinit var notificationsTimeMinutesFlow: StateFlow<Int>
    private var notificationsMinutesTime: Int
        get() = notificationsTimeMinutesFlow.value
        set(value) = update(KEY_NOTIFICATIONS_TIME, value)

    val notificationsTimeFlow by lazy {
        notificationsTimeMinutesFlow.mapState(scope) { minutes ->
            val rawMinutes = minutes % 60
            LocalTime.MIDNIGHT.withHour((minutes - rawMinutes) / 60).withMinute(rawMinutes)
        }
    }
    var notificationsTime: LocalTime
        get() = notificationsTimeFlow.value
        set(value) {
            notificationsMinutesTime = value.hour * 60 + value.minute
        }

}