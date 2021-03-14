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

package cz.lastaapps.president.app.initializers

import android.content.Context
import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.annotation.Keep
import androidx.startup.Initializer
import cz.lastaapps.president.core.BuildConfig
import cz.lastaapps.president.core.InitializerTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

/**
 * Runs at app start
 * Sets up Strict mode output settings for the deprecated APIs
 * */
@Keep
class StricktMode : InitializerTemplate<Any> {

    companion object {
        private val TAG = StricktMode::class.simpleName

        private const val enabled: Boolean = false
        private const val logEnabled: Boolean = false
    }

    override fun create(c: Context): Any {
        logCreate()

        if (BuildConfig.DEBUG) {

            val policy = if (enabled) {

                val scope = CoroutineScope(Dispatchers.Default)
                val executor = Executor {
                    scope.launch {
                        it.run()
                    }
                }

                with(StrictMode.VmPolicy.Builder()) {

                    detectLeakedSqlLiteObjects()
                    detectLeakedClosableObjects()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        detectNonSdkApiUsage()
                        if (logEnabled)
                            penaltyListener(executor) {
                                Log.e(TAG, "StringManager error: ${it.message}", it.cause)
                            }
                    }
                    if (logEnabled)
                        penaltyLog()

                    //.penaltyDeath()

                    build()
                }
            } else {
                StrictMode.VmPolicy.Builder().build()
            }
            StrictMode.setVmPolicy(policy)
        }

        return Any()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        logDependencies()

        return emptyList()
    }
}
