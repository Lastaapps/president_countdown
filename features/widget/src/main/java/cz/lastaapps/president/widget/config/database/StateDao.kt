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

import androidx.room.*
import cz.lastaapps.president.widget.config.WidgetState
import kotlinx.coroutines.flow.Flow

private const val table = WidgetDatabase.TABLE_WIDGET_STATE

@Dao
internal interface StateDao {

    @Query("SELECT * FROM $table")
    fun getAll(): Flow<List<WidgetState>>

    @Query("SELECT * FROM $table WHERE id IS :id")
    fun getById(id: Int): Flow<WidgetState?>

    @Query("SELECT * FROM $table WHERE id IN (:ids)")
    fun getByIds(ids: IntArray): Flow<List<WidgetState>>

    @Query("SELECT id FROM $table")
    suspend fun getAllIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg states: WidgetState)

    @Query("DELETE FROM $table WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Int)

    @Delete
    suspend fun delete(vararg states: WidgetState)

}