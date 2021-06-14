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

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.lastaapps.president.core.App
import cz.lastaapps.president.core.functionality.CET
import cz.lastaapps.president.core.functionality.LocalDateCET
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Long.max
import java.time.LocalTime
import java.time.ZonedDateTime

/**
 * Stores data about president - mandate's state
 * */
object PresidentStateStorage {

    private val TAG get() = PresidentStateStorage::class.simpleName

    private const val DATA_STORE_NAME = "state_data_store"
    private val KEY_STATE = intPreferencesKey("state")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    private val dataStore = App.context.dataStore

    /**
     * Changes presidents state after his mandate has ended
     * */
    init {
        GlobalScope.launch {
            updateStateBasedOnTime()
        }
        restartAutoStateChangeResolver()
    }

    private var autoStateChangeResolver: Job? = null
    internal fun restartAutoStateChangeResolver() {
        autoStateChangeResolver?.cancel()

        autoStateChangeResolver = GlobalScope.launch {
            val mandateEnd = ZonedDateTime.of(President.mandateEnd, LocalTime.MIDNIGHT, CET)
            val left =
                mandateEnd.toInstant().epochSecond - ZonedDateTime.now().toInstant().epochSecond

            delay(max(0L, left * 1000 + 10))

            updateStateBasedOnTime()
        }
    }

    /**
     * Checks if the current state is valid
     * Switches between WORKING and FINISHED states only, if any other is used, this method does nothing
     * */
    internal suspend fun updateStateBasedOnTime() {
        val state = getCurrentState()
        if (LocalDateCET.now() >= President.mandateEnd) {
            if (state == PresidentState.WORKING) {
                setState(PresidentState.FINISHED)
            }
        } else {
            //if somebody plays with the system time
            if (state == PresidentState.FINISHED) {
                setState(PresidentState.WORKING)
            }
        }
    }

    /**
     * Sets state
     * @returns if state has benn changed
     */
    suspend fun setState(state: PresidentState): Boolean {
        val current = getCurrentState()

        //after mandate has ended check
        if (LocalDateCET.now() >= President.mandateEnd && state == PresidentState.WORKING)
            return setState(PresidentState.FINISHED)

        dataStore.edit { settings ->

            Log.i(TAG, "State changed to $state")
            settings[KEY_STATE] = state.code
        }

        return current == state
    }

    /**
     * sets state without blocking the calling thread
     * */
    fun setStateSynchronously(state: PresidentState) {
        GlobalScope.launch {
            setState(state)
        }
    }

    /**
     * @return Current state as a Flow
     * */
    private fun getState(): Flow<PresidentState> =
        dataStore.data.map {
            val code = it[KEY_STATE] ?: PresidentState.WORKING.code
            PresidentState.byCode(code)
        }.distinctUntilChanged()

    /**
     * @return current state once
     * */
    suspend fun getCurrentState() = getState().first()

    /**
     * @return Current state as a StateFlow
     * */
    suspend fun getConsistentState(scope: CoroutineScope): StateFlow<PresidentState> {
        return getState().stateIn(scope)
    }

    /**
     * Blocks to obtain the state
     * @return Current state as a StateFlow
     * */
    fun getConsistentStateAsync(scope: CoroutineScope): StateFlow<PresidentState> {
        return getState().stateIn(scope, SharingStarted.WhileSubscribed(), PresidentState.LOADING)
    }
}