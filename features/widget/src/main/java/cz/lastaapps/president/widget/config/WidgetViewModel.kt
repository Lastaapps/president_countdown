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

package cz.lastaapps.president.widget.config

import android.app.Application
import android.appwidget.AppWidgetManager
import androidx.annotation.IntDef
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.config.database.WidgetDatabase
import cz.lastaapps.president.widget.widget.RemoteViewUpdater
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class WidgetViewModel(private val app: Application) : AndroidViewModel(app) {

    private val context = app

    private val database = WidgetDatabase.createDatabase(app)
    private val repo = database.configRepo


    private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    private var databaseCollectionJob: Job? = null

    fun setWidgetId(widgetId: Int) {

        if (this.widgetId == widgetId)
            return

        this.widgetId = widgetId
        state.tryEmit(state.value.copy(id = widgetId))

        databaseCollectionJob?.cancel()
        databaseCollectionJob = viewModelScope.launch {
            repo.getById(widgetId).collect {
                it?.let {
                    state.emit(it)
                }
            }
        }
    }

    private val state by lazy { MutableStateFlow(WidgetState.createDefault(widgetId)) }
    fun getState(): StateFlow<WidgetState> =
        state.also {
            viewModelScope.launch {
                it.collectLatest {
                    println("New state")
                }
            }
        }

    fun setState(state: WidgetState) {
        this.state.tryEmit(state)
    }

    val uiState = MutableStateFlow<@UIStage Int>(UIStage.CONFIG)

    fun cancel() {
        uiState.tryEmit(UIStage.CANCEL)
    }

    fun ok() {
        uiState.tryEmit(UIStage.PROCESSING)

        viewModelScope.launch {

            val state = state.value

            repo.insertAll(state)

            val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)

            RemoteViewUpdater.createRemoteViews(context).also { views ->

                val countdownState = CurrentState.getCurrentState(this).value

                RemoteViewUpdater.updateState(context, views, countdownState)
                RemoteViewUpdater.updateColors(context, views, state)

                appWidgetManager.updateAppWidget(widgetId, views)
            }

            uiState.tryEmit(UIStage.OK)
        }
    }
}

/**
 * All the possible uiMode combinations
 * */
@IntDef(value = [UIStage.CONFIG, UIStage.OK, UIStage.CANCEL])
@Retention(AnnotationRetention.SOURCE)
internal annotation class UIStage {
    companion object {
        const val CONFIG = 0
        const val OK = 1
        const val CANCEL = 2
        const val PROCESSING = 3
    }
}