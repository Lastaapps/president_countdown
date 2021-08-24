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

package cz.lastaapps.president.widget.core.config

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.res.Configuration
import androidx.annotation.IntDef
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.core.config.database.WidgetDatabase
import cz.lastaapps.president.widget.core.widget.RemoteViewUpdater
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class WidgetViewModel(app: Application) : AndroidViewModel(app) {

    abstract val viewUpdater: RemoteViewUpdater

    private val context = app

    private val database = WidgetDatabase.createDatabase(app)
    private val repo = database.configRepo


    private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    private val state by lazy {
        MutableStateFlow(WidgetState.createDefault(AppWidgetManager.INVALID_APPWIDGET_ID))
    }

    fun getState(): StateFlow<WidgetState> = state
    fun setState(state: WidgetState) {
        this.state.tryEmit(state)
    }

    private val isLightPreview = MutableStateFlow(true)
    fun getIsLightPreview() = isLightPreview
    fun setIsLightPreview(isLight: Boolean) {
        isLightPreview.tryEmit(isLight)
    }


    private var databaseCollectionJob: Job? = null

    /**
     * Load configs from a database for core given
     * */
    fun setWidgetId(widgetId: Int) {

        if (this.widgetId == widgetId)
            return

        this.widgetId = widgetId
        state.tryEmit(state.value.copy(id = widgetId))

        databaseCollectionJob?.cancel()
        databaseCollectionJob = viewModelScope.launch {
            launch {
                repo.getById(widgetId).collect {
                    it?.let {
                        state.emit(it)
                    }
                }
            }

            //loads default preview theme - light or dark - based on saved data or current ui state
            launch database@{

                val databaseState = repo.getById(widgetId).first() ?: return@database

                setIsLightPreview(
                    when (databaseState.theme) {
                        WidgetThemeMode.DAY -> true
                        WidgetThemeMode.NIGHT -> false
                        WidgetThemeMode.SYSTEM ->
                            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_YES == 0
                        else -> return@database
                    }
                )
            }
        }
    }

    /**
     * User's progress in setting up a core
     * */
    val configProgressState = MutableStateFlow<@UIStage Int>(UIStage.CONFIG)

    /**
     * Cancel button pressed
     * */
    fun cancel() {
        configProgressState.tryEmit(UIStage.CANCEL)
    }

    /**
     * Ok button pressed
     * */
    fun ok() {
        configProgressState.tryEmit(UIStage.PROCESSING)

        viewModelScope.launch {

            val state = state.value

            repo.insertAll(state)

            val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(context)

            viewUpdater.createRemoteViews(context, widgetId).also { views ->

                val countdownState = CurrentState.getCurrentState(this).value

                viewUpdater.updateState(context, views, countdownState)
                viewUpdater.updateColors(context, views, state, null)

                appWidgetManager.updateAppWidget(widgetId, views)
            }

            configProgressState.tryEmit(UIStage.OK)
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