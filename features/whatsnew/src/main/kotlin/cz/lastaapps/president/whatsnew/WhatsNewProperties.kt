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

package cz.lastaapps.president.whatsnew

import android.content.Context
import android.content.pm.PackageManager
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cz.lastaapps.president.core.functionality.getVersionCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Holds settings about Whats new module
 * Complex operations
 * */
class WhatsNewProperties private constructor(
    private val context: Context,
    private val scope: CoroutineScope
) {

    companion object {
        private val TAG = WhatsNewProperties::class.simpleName

        private const val DATASTORE_NAME = "whats_new_datastore"
        private val KEY_VERSION_READ = longPreferencesKey("date_read")
        private val KEY_AUTO_LAUNCH = booleanPreferencesKey("auto_launch")
        private val KEY_SHOW_ADVANCED = booleanPreferencesKey("show_advanced")

        private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

        suspend fun getInstance(context: Context, scope: CoroutineScope): WhatsNewProperties {
            return WhatsNewProperties(context, scope).also {
                it.initialize()
            }
        }
    }

    private val dataStore = context.dataStore

    /**
     * Creates state flows from datastore flows
     * */
    suspend fun initialize() {
        versionReadFlow = mapDataStore(KEY_VERSION_READ, 0)
        autoLaunchFlow = mapDataStore(KEY_AUTO_LAUNCH, false)
        showAdvancedFlow = mapDataStore(KEY_SHOW_ADVANCED, false)
    }

    private suspend fun <T> mapDataStore(key: Preferences.Key<T>, default: T): StateFlow<T> =
        dataStore.data.map { it[key] ?: default }.distinctUntilChanged().stateIn(scope)

    private fun <T> update(key: Preferences.Key<T>, value: T) {
        scope.launch {
            dataStore.edit {
                it[key] = value
            }
        }
    }

    private lateinit var versionReadFlow: StateFlow<Long>
    private var versionRead: Long
        get() = versionReadFlow.value
        set(value) = update(KEY_VERSION_READ, value)

    lateinit var autoLaunchFlow: StateFlow<Boolean>
    var autoLaunch: Boolean
        get() = autoLaunchFlow.value
        set(value) = update(KEY_AUTO_LAUNCH, value)

    lateinit var showAdvancedFlow: StateFlow<Boolean>
    var showAdvanced: Boolean
        get() = showAdvancedFlow.value
        set(value) = update(KEY_SHOW_ADVANCED, value)

    /**
     * The dialog has been shown, no need to show it again
     * */
    fun updateVersion() {
        try {
            versionRead = context.getVersionCode()

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * @return if there is new version and user requires it
     * */
    fun shouldAutoShow(): Boolean {
        if (autoLaunch && (versionRead < context.getVersionCode())) {
            if ((BuildConfig.isAlpha || BuildConfig.isBeta) == showAdvanced)
                return true
        }
        return false
    }

}
