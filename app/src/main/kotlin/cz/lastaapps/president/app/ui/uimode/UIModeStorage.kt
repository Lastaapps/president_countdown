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

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Stores data about user preferred ui mode for context given (represented by the name)
 * */
class UIModeStorage(private val context: Context, private val name: String) {

    companion object {
        private const val PREFIX = "UI_MODE_STORAGE_"
        private const val KEY_UI_MODE = "UI_MODE"
    }

    private val sp get() = context.getSharedPreferences(PREFIX + name, Context.MODE_PRIVATE)

    private val uiMode = MutableStateFlow(sp.getInt(KEY_UI_MODE, UIModeState.SYSTEM))

    fun getThemeFlow(): StateFlow<Int> = uiMode

    fun setTheme(@UIModeState state: Int) {
        uiMode.tryEmit(state)
        sp.edit().putInt(KEY_UI_MODE, state).apply()
    }
}
