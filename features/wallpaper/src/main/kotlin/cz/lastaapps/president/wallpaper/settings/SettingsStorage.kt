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

package cz.lastaapps.president.wallpaper.settings

//to slow implementation using data store
/*

import android.content.res.Configuration
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import cz.lastaapps.president.core.App
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal object SettingsStorage {

    private const val DATA_STORE_NAME = "wallpaper_settings_data_store"
    private val biasVertPortKey = floatPreferencesKey("biasVertPort")
    private val biasHorzPortKey = floatPreferencesKey("biasHorzPort")
    private val biasVertLandKey = floatPreferencesKey("biasVertLand")
    private val biasHorzLandKey = floatPreferencesKey("biasHorzLand")
    private val scalePortKey = floatPreferencesKey("scalePort")
    private val scaleLandKey = floatPreferencesKey("scaleLand")
    private val uiModeKey = intPreferencesKey("uiMode")
    private val rotationFixKey = booleanPreferencesKey("rotationFix")

    private val dataStore = App.context.createDataStore(DATA_STORE_NAME)

    fun getBiasVertPort() = dataStore.data.map { it[biasVertPortKey] ?: .5f }
    fun getBiasHorzPort() = dataStore.data.map { it[biasHorzPortKey] ?: .5f }
    fun getBiasVertLand() = dataStore.data.map { it[biasVertLandKey] ?: .5f }
    fun getBiasHorzLand() = dataStore.data.map { it[biasHorzLandKey] ?: .5f }
    fun getScalePort() = dataStore.data.map { it[scalePortKey] ?: 1f }
    fun getScaleLand() = dataStore.data.map { it[scaleLandKey] ?: 1f }
    fun getUIMode() = dataStore.data.map { it[uiModeKey] ?: Configuration.UI_MODE_NIGHT_UNDEFINED }
    fun getRotationFix() = dataStore.data.map { it[rotationFixKey] ?: false }

    suspend fun setBiasVertPort(value: Float) = dataStore.edit { it[biasVertPortKey] = value }
    suspend fun setBiasHorzPort(value: Float) = dataStore.edit { it[biasHorzPortKey] = value }
    suspend fun setBiasVertLand(value: Float) = dataStore.edit { it[biasVertLandKey] = value }
    suspend fun setBiasHorzLand(value: Float) = dataStore.edit { it[biasHorzLandKey] = value }
    suspend fun setScalePort(value: Float) = dataStore.edit { it[scalePortKey] = value }
    suspend fun setScaleLand(value: Float) = dataStore.edit { it[scaleLandKey] = value }
    suspend fun setUIMode(value: Int) = dataStore.edit { it[uiModeKey] = value }
    suspend fun setRotationFix(value: Boolean) = dataStore.edit { it[rotationFixKey] = value }
}
*/
