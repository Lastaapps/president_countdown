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

package cz.lastaapps.president.notifications.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.lastaapps.president.notifications.NotificationsConfig
import cz.lastaapps.president.notifications.settings.Settings
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalTime

internal class NotifyViewModel(private val app: Application) : AndroidViewModel(app) {

    private lateinit var settings: Settings

    private val initMutex = Mutex()

    suspend fun initializeSettings() = initMutex.withLock {
        if (!this::settings.isInitialized)
            settings = Settings.getInstance(app, viewModelScope)
    }

    val announcementsFlow by lazy { settings.announcementsFlow }
    var announcements: Boolean
        get() = announcementsFlow.value
        set(value) {
            settings.announcements = value
            updateConfigs()
        }

    val dailyFlow by lazy { settings.dailyFlow }
    var daily: Boolean
        get() = dailyFlow.value
        set(value) {
            settings.daily = value
            updateConfigs()
        }

    val permanentFlow by lazy { settings.permanentFlow }
    var permanent: Boolean
        get() = permanentFlow.value
        set(value) {
            settings.permanent = value
            updateConfigs()
        }

    val notificationsTimeFlow by lazy { settings.notificationsTimeFlow }
    var notificationsTime: LocalTime
        get() = notificationsTimeFlow.value
        set(value) {
            settings.notificationsTime = value
            updateConfigs()
        }

    private fun updateConfigs() {
        viewModelScope.launch {
            NotificationsConfig.initAll(app, viewModelScope)
        }
    }
}