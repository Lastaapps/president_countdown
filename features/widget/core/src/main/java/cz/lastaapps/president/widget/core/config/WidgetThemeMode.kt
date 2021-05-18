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

package cz.lastaapps.president.widget.core.config

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.IntDef

/**
 * All the possible uiMode combinations
 * */
@IntDef(value = [WidgetThemeMode.SYSTEM, WidgetThemeMode.DAY, WidgetThemeMode.NIGHT])
@Retention(AnnotationRetention.SOURCE)
internal annotation class WidgetThemeMode {
    companion object {

        const val SYSTEM = 0
        const val DAY = 1
        const val NIGHT = 2

        /**
         * @return if the sate given should be displayed as light color. Respects system settings
         * */
        fun isLight(context: Context, @WidgetThemeMode mode: Int): Boolean {
            return when (mode) {
                DAY -> true
                NIGHT -> false
                SYSTEM -> context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_YES == 0
                else -> throw IllegalArgumentException("Illegal argument, not a valid ${WidgetThemeMode::class.simpleName}")
            }
        }
    }
}