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

import android.content.Context
import cz.lastaapps.president.core.App
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Stores widget settings
 * */
internal object Settings {

    private val TAG = Settings::class.simpleName

    private const val SP_NAME = "WIDGET_COMMON_SETTINGS"
    private const val KEY_DEBUG = "DEBUG"

    private val sp
        get() = App.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

    //debug mode prints notification updates about updating the widget - is the service running?
    var debug: Boolean
        get() = sp.getBoolean(KEY_DEBUG, false)
        set(value) {
            sp.edit().putBoolean(KEY_DEBUG, value).apply()
            debugFlow.tryEmit(value)
        }
    val debugFlow by lazy { MutableStateFlow(debug) }

}