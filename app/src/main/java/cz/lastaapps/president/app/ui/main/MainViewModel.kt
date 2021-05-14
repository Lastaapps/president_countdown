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

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.lastaapps.president.app.BuildConfig
import cz.lastaapps.president.app.R
import cz.lastaapps.president.widget.WidgetConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    /**Holds the text between config changes*/
    val snackbarMessage: MutableStateFlow<String?> = MutableStateFlow(null)

    fun showSnackbar(message: String?) {
        snackbarMessage.tryEmit(message)
    }

    /**Switches hidden widget debugging feature*/
    fun toggleWidgetDebugging(context: Context) {

        if (BuildConfig.DEBUG) {
            val newState = WidgetConfig.toggleDebug()
            showSnackbar(
                context.getString(if (newState) R.string.widget_debug_enabled else R.string.widget_debug_disabled)
            )
        }
    }

    // shows user that there is a small arrow to open config menu
    private val menuOpened = MenuOpened(app)
    val isOpened = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            menuOpened.getState().distinctUntilChanged().collectLatest {
                isOpened.tryEmit(it)
            }
        }
    }

    fun markOpened() {
        viewModelScope.launch {
            menuOpened.markOpened()
        }
    }
}