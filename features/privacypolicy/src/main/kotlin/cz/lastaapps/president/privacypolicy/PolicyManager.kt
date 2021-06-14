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

package cz.lastaapps.president.privacypolicy

import android.content.Context
import java.time.*

/**
 * Stores data if user has agreed
 * */
internal class PolicyManager(context: Context) {

    companion object {
        private val TAG = PolicyManager::class.simpleName

        private const val SP_NAME = "PRIVACY_POLICY"
        private const val SP_DATE = "DATE"
        private val POLICY_CREATED =
            ZonedDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.MIDNIGHT, ZoneId.of("UTC"))
    }

    /**Saves now to storage representing that user agreed*/
    internal fun userAgreed() {
        sp.edit().putLong(SP_DATE, ZonedDateTime.now().toInstant().epochSecond).apply()
    }

    /**@return If user has agreed*/
    fun shouldAgree(): Boolean {
        val seconds = sp.getLong(SP_DATE, -1)
        if (seconds < 0) return true

        val date = ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.of("UTC"))

        return date < POLICY_CREATED
    }

    /**SharedPreferences with agreed info*/
    private val sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
}