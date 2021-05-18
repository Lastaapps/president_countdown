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

package cz.lastaapps.president.widget.wrapper

import android.content.ComponentName
import android.content.Context
import cz.lastaapps.president.widget.core.config.WidgetConfigActivity
import cz.lastaapps.president.widget.core.widget.Widget
import cz.lastaapps.president.widget.wrapper.service.DebugSettings
import cz.lastaapps.president.widget.wrapper.service.WidgetUpdateService
import kotlinx.coroutines.delay

/**
 * Entry point for the module
 * */
object WidgetConfig {

    suspend fun instantiateModule(context: Context) {
        WidgetConfigActivity.startService = { updateService(it) }
        Widget.allComponents = WidgetsCombiner.providers.map { ComponentName(context, it.java) }

        //saving startup resources
        delay(3000)
        updateService(context)
    }

    fun updateService(context: Context) {
        WidgetUpdateService.startService(context)
    }

    /**Toggles core debug mode
     * @return new state*/
    fun toggleDebug(): Boolean {
        return (!DebugSettings.debugFlow.value).also {
            DebugSettings.debug = it
        }
    }
}