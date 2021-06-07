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

package cz.lastaapps.president.widget.core.config.database

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cz.lastaapps.president.widget.core.config.WidgetState
import java.util.concurrent.TimeUnit

@Database(
    entities = [WidgetState::class],
    version = 4,
    exportSchema = true,
)
abstract class WidgetDatabase : RoomDatabase() {

    protected abstract fun configDao(): StateDao
    val configRepo by lazy { WidgetRepo(this, configDao()) }

    companion object {

        private val TAG get() = WidgetDatabase::class.simpleName!!

        //tables
        const val TABLE_WIDGET_STATE = "widget_state"

        private const val DATABASE_NAME = "widget_config"
        private var database: WidgetDatabase? = null

        @SuppressLint("UnsafeOptInUsageError")
        fun createDatabase(context: Context): WidgetDatabase = synchronized(this) {

            database?.let {
                return it
            }

            with(
                Room.databaseBuilder(
                    context.applicationContext, WidgetDatabase::class.java, DATABASE_NAME
                )
            ) {
                fallbackToDestructiveMigration()
                setAutoCloseTimeout(5, TimeUnit.MINUTES)
                addCallback(Listener(TAG))
                build()
            }.also {
                database = it
            }
        }
    }
}

private class Listener(private val TAG: String) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        Log.i(TAG, "Creating database")
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        Log.i(TAG, "Opening database")
    }

    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
        Log.e(TAG, "Destroying database because of migration!")
    }
}
