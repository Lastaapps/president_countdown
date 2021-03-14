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

package cz.lastaapps.president.app.initializers

import android.content.Context
import androidx.annotation.Keep
import androidx.startup.Initializer
import cz.lastaapps.president.core.App
import cz.lastaapps.president.core.InitializerTemplate
import cz.lastaapps.president.notifications.NotificationsConfig
import kotlinx.coroutines.delay

/**
 * Runs at app start
 * Throws some tasks to execute after the app object is created
 * */
@Keep
class RunAfterAppInitializer : InitializerTemplate<Any> {

    override fun create(c: Context): Any {
        logCreate()

        App.afterAppInitialization.addAll(
            listOf(
                { context, scope ->
                    //lazy init
                    delay(5000)

                    NotificationsConfig.initAll(context, scope)
                },
                { _, _ -> },
            )
        )

        return Any()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        logDependencies()

        return emptyList()
    }
}