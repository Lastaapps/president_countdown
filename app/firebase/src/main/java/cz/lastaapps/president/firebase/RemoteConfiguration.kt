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

package cz.lastaapps.president.firebase

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import cz.lastaapps.president.core.president.PresidentState
import cz.lastaapps.president.core.president.PresidentStateStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Fetches remote configs from firebase servers
 * Controls the state of president's mandate
 */
object RemoteConfiguration {

    private val TAG = RemoteConfiguration::class.simpleName
    private const val STATE_ID = "stateID"
    private const val FETCH_INTERVAL = 6 * 3600L

    private val mFirebaseRemoteConfig: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    init {

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(FETCH_INTERVAL)
            .build()
        mFirebaseRemoteConfig.also {
            it.setConfigSettingsAsync(configSettings)
            it.setDefaultsAsync(R.xml.remote_config_defaults)
        }
    }

    suspend fun fetch() {
        //converts the callback into suspendable function
        suspendCancellableCoroutine<Boolean> { continuation ->

            Log.i(TAG, "Fetching data")
            val task = mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i(TAG, "Fetch succeeded")

                        /*Toast.makeText(context, "Fetch succeeded",
                                    Toast.LENGTH_SHORT).show();*/

                        mFirebaseRemoteConfig.activate()
                        continuation.resume(true)
                    } else {
                        Log.e(TAG, "Fetch failed")
                        /*Toast.makeText(context, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();*/
                        continuation.resume(false)
                    }

                    //uses new data from the database
                    PresidentStateStorage.setStateSynchronously(
                        PresidentState.byCode(
                            mFirebaseRemoteConfig.getLong(STATE_ID).toInt()
                        )
                    )
                }.addOnCanceledListener {
                    continuation.cancel()
                }

            if (task.isCanceled)
                continuation.cancel()
        }
        Log.i(TAG, "Returning from continuation")
    }
}