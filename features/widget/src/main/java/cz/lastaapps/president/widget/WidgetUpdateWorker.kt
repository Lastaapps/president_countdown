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

package cz.lastaapps.president.widget

/*
import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation


//.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)

class WidgetUpdateWorker(private val context: Context, private val params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        private val TAG get() = WidgetUpdateWorker::class.simpleName

        private const val NOTIFICATION_ID = 49250
        private const val NOTIFICATION_DEBUG_STATE_ID = 49251
        private const val NOTIFICATION_DEBUG_UPDATED_ID = 49252
    }

    private val active = MutableStateFlow(true)
    private lateinit var continuation: Continuation<Any>

    override suspend fun doWork(): Result {

        initialize()



        suspendCancellableCoroutine<Any> {
            continuation = it
        }

        destroy()

        return Result.success()
    }

    private suspend fun initialize() {
        //creates a notification channel
        Notifications(context)

    }

    private suspend fun destroy() {

    }



    @SuppressLint("UnsafeExperimentalUsageError")
    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }
}*/