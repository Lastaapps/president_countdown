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

import cz.lastaapps.president.core.functionality.CET
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

/**
 * enables access similar to arrays
 * starts with years
 * */
operator fun CurrentState.get(index: Int): Long =
    when (index) {
        0 -> years
        1 -> days
        2 -> hours
        3 -> minutes
        4 -> seconds
        else -> 0
    }

/**
 * Represents the current state of the president's mandate with time left if available
 * */
data class CurrentState(
    val state: PresidentState,
    val years: Long = 0,
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0,
) {

    //string representation
    fun sByIndex(index: Int) = this[index].toString()

    val sYears get() = years.toString()
    val sDays get() = days.toString()
    val sHours get() = hours.toString()
    val sMinutes get() = minutes.toString()
    val sSeconds get() = seconds.toString()

    companion object {

        /**
         * Flow with states emitting new value every second
         * */
        fun getCurrentState(coroutineScope: CoroutineScope): StateFlow<CurrentState> {

            val state = PresidentStateStorage.getConsistentStateAsync(coroutineScope)
            //start state loading
            coroutineScope.launch(Dispatchers.Default) { state.collect {} }

            return flow {
                while (true) {
                    emit(createCurrentState(state.value))

                    delay(1000 - (System.currentTimeMillis() % 1000) + 10)
                }
            }.stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(),
                createCurrentState(state.value)
            )
        }

        /**
         * @return flow of states with 3 as default in the buffer
         * */
        fun getCurrentBuffered(scope: CoroutineScope, bufferSize: Int = 3) =
            getCurrentState(scope).shareIn(scope, SharingStarted.WhileSubscribed(), bufferSize)

        /**
         * @return current state
         *  */
        fun getCurrentStateOnceAsync(): CurrentState = runBlocking(Dispatchers.Default) {
            createCurrentState(PresidentStateStorage.getCurrentState())
        }

        /**Creates the object based on the newest data*/
        fun createCurrentState(
            state: PresidentState,
            now: ZonedDateTime = ZonedDateTime.now()
        ): CurrentState {

            val mandateEnd = ZonedDateTime.of(President.mandateEnd, LocalTime.MIDNIGHT, CET)

            if (mandateEnd < now || !state.isTimeRemainingSupported) {
                return CurrentState(state, 0, 0, 0, 0, 0)
            }

            var toUse = now
            val years = ChronoUnit.YEARS.between(toUse, mandateEnd)
            toUse = toUse.plusYears(years)
            val days = ChronoUnit.DAYS.between(toUse, mandateEnd)
            toUse = toUse.plusDays(days)
            val hours = ChronoUnit.HOURS.between(toUse, mandateEnd)
            toUse = toUse.plusHours(hours)
            val minutes = ChronoUnit.MINUTES.between(toUse, mandateEnd)
            toUse = toUse.plusMinutes(minutes)
            val seconds = ChronoUnit.SECONDS.between(toUse, mandateEnd)

            return CurrentState(state, years, days, hours, minutes, seconds)
        }
    }
}
