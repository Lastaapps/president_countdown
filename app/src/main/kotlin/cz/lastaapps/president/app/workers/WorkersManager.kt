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

package cz.lastaapps.president.app.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import cz.lastaapps.president.firebase.workers.FirebaseConfigWorker
import java.util.concurrent.TimeUnit

/**
 * Schedules all the workers
 * */
object WorkersManager {

    private val TAG = WorkersManager::class.simpleName

    fun register(context: Context) {

        Log.i(TAG, "Registering workers")

        val requests = listOf(
            registerConfigs(),
        )

        val mgr = WorkManager.getInstance(context)

        for (request in requests)
            mgr.enqueue(request)
    }

    private fun registerConfigs(): WorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return PeriodicWorkRequestBuilder<FirebaseConfigWorker>(
            FirebaseConfigWorker.REPEAT_INTERVAL_HOURS,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()
    }

}