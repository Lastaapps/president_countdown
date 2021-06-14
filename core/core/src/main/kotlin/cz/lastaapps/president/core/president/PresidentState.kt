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

package cz.lastaapps.president.core.president

import cz.lastaapps.president.core.R

/**
 * All the state possible for the president to be in
 * */
enum class PresidentState(val code: Int, val stringId: Int, val formattedStringId: Int) {
    WORKING(0, R.string.working, R.string.working_formatted),
    FINISHED(1, R.string.finished, R.string.finished_formatted),
    LEFT(2, R.string.left, R.string.left_formatted),
    DIED(3, R.string.died, R.string.died_formatted),
    LOADING(4, R.string.loading, R.string.loading_formatted),
    UNKNOWN(-1, R.string.error, R.string.error_formatted), ;

    val isTimeRemainingSupported
        get() = this in listOf(WORKING)

    companion object {

        /**
         * This gonna be only called when translating code saved on storage
         * or obtained from firebase remote config server
         * @return state for code given.
         * */
        fun byCode(code: Int): PresidentState {
            values().forEach {
                if (it.code == code)
                    return it
            }
            return UNKNOWN
        }
    }
}