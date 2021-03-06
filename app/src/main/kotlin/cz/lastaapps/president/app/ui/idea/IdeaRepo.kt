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

package cz.lastaapps.president.app.ui.idea

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class IdeaRepo(private val context: Context, private val scope: CoroutineScope) {

    companion object {

        private const val NAME = "IDEA_STORE"
        private val shownKey get() = "SHOWN"

    }

    val isReady = MutableStateFlow(false)

    private val sp get() = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    private lateinit var shown: MutableStateFlow<Boolean>

    init {
        scope.launch {
            shown = MutableStateFlow(sp.getBoolean(shownKey, false))
            isReady.emit(true)
        }
    }

    fun shouldShow(): Boolean = !shown.value

    fun shown() {
        shown.tryEmit(true)
        sp.edit {
            putBoolean(shownKey, true)
        }
    }
}