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

package cz.lastaapps.president.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Maps a StateFlow into another StateFlow with default value
 * */
fun <T, O> StateFlow<T>.mapState(scope: CoroutineScope, convertor: (T) -> O): StateFlow<O> {
    return this.map { convertor(it) }
        .stateIn(scope, SharingStarted.WhileSubscribed(), convertor(this.value))
}

/**
 * Emits all the values in Flow into MutableStateFlow
 * */
fun <T> Flow<T>.moveTo(scope: CoroutineScope, flow: MutableStateFlow<T>) {
    scope.launch {
        collect {
            flow.emit(it)
        }
    }
}

/**
 * Calls collectAsync on the flow. Used to make the code readable and more compact
 * */
fun <T> Flow<T>.collectAsync(scope: CoroutineScope, action: suspend (value: T) -> Unit) {
    scope.launch {
        this@collectAsync.collectLatest(action)
    }
}