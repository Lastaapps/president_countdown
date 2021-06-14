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

import android.app.Notification
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.lastaapps.president.core.functionality.LocalDateCET
import cz.lastaapps.president.core.president.President
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnnouncementsNotificationsTest {

    private lateinit var notifications: AnnouncementsNotifications

    @Before
    fun setupNotifications() {
        notifications = AnnouncementsNotifications(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun createBirthday() {
        val date = President.birthDate
        notifications.createBirthday(date)
    }

    @Test
    fun createNameday() {
        val date = LocalDateCET.now()
        notifications.createNameday(date)
    }

    @Test
    fun createElected_Before() {
        val date = President.mandateStart
        notifications.createElected(date)
    }

    @Test
    fun createElected() {
        val date = President.mandateStart.withYear(LocalDateCET.now().year)
        notifications.createElected(date)
    }

    @Test
    fun createElected_After() {
        val date = President.mandateStart.plusYears(150)
        notifications.createElected(date)
    }

    @Test
    fun createRemains_Before() {
        val date = President.mandateEnd
        notifications.createRemains(date)
    }

    @Test
    fun createRemains() {
        val date = President.mandateEnd.withYear(LocalDateCET.now().year)
        notifications.createRemains(date)
    }

    @Test
    fun createRemains_After() {
        val date = President.mandateEnd.plusYears(150)
        notifications.createRemains(date)
    }
}

internal fun Notification.getTitle(): String = extras.getString("android.title")!!
internal fun Notification.getText(): String = extras.getString("android.text")!!