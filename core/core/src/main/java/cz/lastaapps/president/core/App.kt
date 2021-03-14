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

package cz.lastaapps.president.core

import android.app.Application
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class App : Application() {

    companion object {

        private val TAG = App::class.simpleName

        //static context reference
        private lateinit var mApp: App
        val app get() = mApp
        val context: Context get() = app

        val applicationScope = CoroutineScope(Dispatchers.Main)

        /**tasks run after the App object is created*/
        val afterAppInitialization = ArrayList<suspend (Context, CoroutineScope) -> Unit>()
    }

    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "Creating the application")

        mApp = this

        runInitialization()
    }

    private fun runInitialization() = applicationScope.launch {
        Log.i(TAG, "Running after app initialization")

        for (code in afterAppInitialization) {
            code(this@App, applicationScope)
            yield()
        }

        afterAppInitialization.clear()
    }
}