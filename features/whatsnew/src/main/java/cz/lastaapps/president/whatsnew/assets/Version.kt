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

package cz.lastaapps.president.whatsnew.assets

import android.content.Context
import cz.lastaapps.president.core.functionality.getLocale
import java.time.LocalDate

private const val defaultLocale = "en-US"

/**
 * Represents app version message info
 * */
internal data class Version(
    val name: String,
    val buildNumber: Long,
    val releasedDate: LocalDate,
    val isBeta: Boolean,
    val isAlpha: Boolean,
    private val contents: HashMap<String, String>
) : Comparable<Version> {

    override fun compareTo(other: Version): Int {

        val dateComparison = -1 * releasedDate.compareTo(other.releasedDate)

        if (dateComparison != 0) return dateComparison

        when {
            isAlpha && !other.isAlpha -> return -1
            !isAlpha && other.isAlpha -> return 1
        }

        when {
            isBeta && !other.isBeta -> return -1
            !isBeta && other.isBeta -> return 1
        }

        return 0
    }

    fun getLocalizedContent(context: Context): String {
        val locale = context.getLocale()

        val localeName = locale.toLanguageTag()

        return contents[localeName] ?: contents[defaultLocale]!!
    }
}

internal fun List<Version>.filterAlpha() = filter { true }
internal fun List<Version>.filterBeta() = filter { !it.isAlpha }
internal fun List<Version>.filterGeneral() = filter { !it.isAlpha && !it.isBeta }

