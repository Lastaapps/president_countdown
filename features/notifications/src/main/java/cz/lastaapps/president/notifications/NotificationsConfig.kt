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

import android.app.Activity
import android.content.Context
import cz.lastaapps.president.notifications.announcements.AnnouncementsPlanner
import cz.lastaapps.president.notifications.daily.DailyPlanner
import cz.lastaapps.president.notifications.permanent.PermanentService
import cz.lastaapps.president.notifications.ui.settings.NotificationSettingsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

/**
 * The Entry point to manage the whole module
 * */
object NotificationsConfig {

    /**Used to create pending intents in notifications*/
    var mainActivity: KClass<out Activity>? = null

    /**
     * Set's up all the alarms and starts permanent notification service
     * */
    suspend fun initAll(context: Context) {

        val settings = NotificationSettingsRepo.getInstance(context)

        withContext(Dispatchers.Main) {
            AnnouncementsPlanner(context).plan(settings)
            DailyPlanner(context).plan(settings)
            PermanentService.startService(context, settings)
        }
    }
}