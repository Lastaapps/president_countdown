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

package cz.lastaapps.president.app.ui.rate

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.ZonedDateTime

class RateRepo(private val context: Context) {

    companion object {

        private const val PREFERENCES_NAME = "RARE_POPUP"
        private const val FIRST_OPENED = "FIRST_OPENED"
        private const val SHOWN = "SHOWN"

        private const val DAYS_TO_SHOW = 5

    }

    val state = MutableStateFlow(false)

    private val sp get() = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun appOpened() {

        val opened = sp.getLong(FIRST_OPENED, 0)

        if (opened == 0L) {
            sp.edit {
                putLong(FIRST_OPENED, ZonedDateTime.now().toInstant().epochSecond)
            }
        } else {

            val diff = ZonedDateTime.now().toInstant().epochSecond - opened

            if (diff > DAYS_TO_SHOW * 24 * 3600) {
                if (!sp.getBoolean(SHOWN, false)) {
                    state.tryEmit(true)
                }
            }
        }
    }

    fun shown() {
        state.tryEmit(false)
        sp.edit {
            putBoolean(SHOWN, true)
        }
    }
}