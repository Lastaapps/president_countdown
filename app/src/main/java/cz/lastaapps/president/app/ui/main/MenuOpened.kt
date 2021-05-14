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

package cz.lastaapps.president.app.ui.main

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MenuOpened(private val context: Context) {

    private val name = "menu_opened"
    private val openedKey = "opened"
    private val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private val state = MutableStateFlow(sp.getBoolean(openedKey, false))
    fun getState(): Flow<Boolean> = state
    suspend fun setState(value: Boolean) {
        sp.edit().putBoolean(openedKey, value).apply()
        state.tryEmit(value)
    }

    /*
    private val datastoreName = "menu_opened_data_store"
    private val openedKey = booleanPreferencesKey("opened")

    private val Context.dataStore by preferencesDataStore(datastoreName)
    private val dataStore get() = context.dataStore

    fun getState(): Flow<Boolean> {
        return dataStore.data.map { it[openedKey] ?: false }
    }

    suspend fun setState(value: Boolean) {
        dataStore.edit {
            it[openedKey] = value
        }
    }
*/

    suspend fun markOpened() = setState(true)
}