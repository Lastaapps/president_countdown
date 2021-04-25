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

package cz.lastaapps.president.widget.config.database

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import cz.lastaapps.president.widget.config.WidgetState
import cz.lastaapps.president.widget.widget.PresidentWidget
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Maps DAO's to be accesible for the rest of the app in a meaningful way
 * */
internal class WidgetRepo(private val database: WidgetDatabase, private val dao: StateDao) {

    companion object {
        private val TAG get() = WidgetRepo::class.simpleName
    }

    suspend fun updateWidgets(context: Context) {
        val mgr = AppWidgetManager.getInstance(context)

        val validIds = mgr.getAppWidgetIds(ComponentName(context, PresidentWidget::class.java))
        val savedIds = dao.getAllIds()

        val toDelete = mutableListOf<Int>()

        savedIds.forEach {
            if (!validIds.contains(it))
                toDelete += it
        }

        if (toDelete.isNotEmpty()) {
            Log.d(TAG, "Deleting old widget states: $toDelete")
            dao.delete(*toDelete.toIntArray())
        }
    }

    fun getAll(): Flow<List<WidgetState>> = dao.getAll().distinctUntilChanged()

    fun getById(id: Int): Flow<WidgetState?> = dao.getById(id).distinctUntilChanged()

    fun getByIds(ids: IntArray): Flow<List<WidgetState>> = dao.getByIds(ids).distinctUntilChanged()

    suspend fun insertAll(vararg states: WidgetState) = dao.insertAll(*states)

}