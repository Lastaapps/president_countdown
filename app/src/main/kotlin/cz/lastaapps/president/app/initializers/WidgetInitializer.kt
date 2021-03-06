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
import cz.lastaapps.president.app.MainActivity
import cz.lastaapps.president.core.App
import cz.lastaapps.president.core.InitializerTemplate
import cz.lastaapps.president.widget.wrapper.WidgetConfig

/**
 * Runs at app start
 * Notifies core's and starts appropriate service in necessary
 * */
@Keep
class WidgetInitializer : InitializerTemplate<Any> {

    override fun create(context: Context): Any {
        logCreate()

        WidgetConfig.instantiateModule(context, MainActivity::class)

        //delays launch to speedup startup time
        App.afterAppInitialization.add { appContext, _ ->
            WidgetConfig.runModule(appContext)
        }

        return Any()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        logDependencies()

        return emptyList()
    }
}