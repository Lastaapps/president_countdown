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

package cz.lastaapps.president.clock

import android.content.Context

/**
 * Shortcut for plurals for time names - year, day, hour, minute, second
 * */
class TimePlurals(private val context: Context) {

    fun getYears(quantity: Int): String =
        context.resources.getQuantityString(R.plurals.year, quantity)

    fun getDays(quantity: Int): String =
        context.resources.getQuantityString(R.plurals.day, quantity)

    fun getHours(quantity: Int): String =
        context.resources.getQuantityString(R.plurals.hour, quantity)

    fun getMinutes(quantity: Int): String =
        context.resources.getQuantityString(R.plurals.minute, quantity)

    fun getSeconds(quantity: Int): String =
        context.resources.getQuantityString(R.plurals.second, quantity)

    fun getByIndex(index: Int, quantity: Int) = when (index) {
        0 -> getYears(quantity)
        1 -> getDays(quantity)
        2 -> getHours(quantity)
        3 -> getMinutes(quantity)
        4 -> getSeconds(quantity)
        else -> "(•_•)"
    }
}