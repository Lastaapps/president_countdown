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

package cz.lastaapps.president.whatsnew.json

import android.content.Context
import cz.lastaapps.president.core.functionality.getLocale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate

/**
 * Loads version update info from storage
 * */
internal class Loader(private val context: Context) {

    suspend fun load(): List<Version> {

        val text = withContext(Dispatchers.IO) {
            BufferedReader(InputStreamReader(openStream())).readText()
        }

        val root = JSONObject(text)
        val array = root.getJSONArray("versions")

        val list = ArrayList<Version>()

        for (i in 0 until array.length()) {
            val json = array.getJSONObject(i)

            list.add(
                Version(
                    json.getLong("buildNumber"),
                    json.getString("name"),
                    json.getLocalDate("releaseDate"),
                    json.getString("content"),
                )
            )

            yield()
        }

        return list
    }

    /**
     * @return stream to whats new asses based on the current locale
     * */
    private fun openStream(): InputStream {
        return try {
            val language = context.getLocale().language
            context.assets.open("whats_new_${language}.json")
        } catch (e: Exception) {
            context.assets.open("whats_new.json")
        }
    }

    private fun JSONObject.getLocalDate(name: String): LocalDate {
        return LocalDate.parse(getString(name))
    }
}