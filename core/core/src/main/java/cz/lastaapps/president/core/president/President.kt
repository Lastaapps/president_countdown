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

package cz.lastaapps.president.core.president

import java.time.LocalDate
import java.time.Month
import java.util.*

/**
 * Basic static info about the current president
 * */
object President {

    //if the president is a woman, change strings from 'he' to 'she'
    const val name = "Miloš Zeman"

    val birthDate: LocalDate = LocalDate.of(1944, Month.SEPTEMBER, 28)
    val namesDate: LocalDate = LocalDate.of(1944, Month.JANUARY, 25)
    val elected: LocalDate = LocalDate.of(2018, Month.JANUARY, 27)
    val mandateStart: LocalDate = LocalDate.of(2018, Month.MARCH, 8)
    val mandateEnd: LocalDate = LocalDate.of(2023, Month.MARCH, 8 + 1)


    private const val wikiPage = "wikipedia.org/wiki/Miloš_Zeman"

    /**
     * Generates link based on the current locale
     * en.wikipedia.com/... for en locale
     * cs.wikipedia.com/... for cs locale
     * Wouldn't work for some exotic locales, but who cares...
     * */
    fun getWikiLink(locale: Locale): String {
        //https://cs.wikipedia.org/wiki/Miloš_Zeman
        return "https://${locale.language}.$wikiPage"
    }

}