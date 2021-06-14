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

package cz.lastaapps.president.app.ui.uimode

import androidx.annotation.IntDef
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * All the possible uiMode combinations
 * */
@IntDef(value = [UIModeState.SYSTEM, UIModeState.DAY, UIModeState.NIGHT])
@Retention(AnnotationRetention.SOURCE)
internal annotation class UIModeState {
    companion object {

        const val SYSTEM = 0
        const val DAY = 1
        const val NIGHT = 2

        //used in next(Int): Int
        private const val lowest = SYSTEM
        private const val highest = NIGHT

        /**
         * @return if the sate given should be displayed as light color. Respects system settings
         * */
        @Composable
        fun isLight(@UIModeState mode: Int): Boolean {
            return when (mode) {
                DAY -> true
                NIGHT -> false
                SYSTEM -> !isSystemInDarkTheme()
                else -> throw IllegalArgumentException("Illegal argument, not a valid ${UIModeState::class.simpleName}")
            }
        }

        /**moves to the next mode in the row*/
        fun next(@UIModeState mode: Int): Int {
            val next = mode + 1
            return when {
                next > highest -> lowest
                next < lowest -> highest
                else -> next
            }
        }
    }
}
