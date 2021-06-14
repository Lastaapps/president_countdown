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

package cz.lastaapps.president.wallpaper.settings.options

import androidx.compose.ui.graphics.Color

data class ClockThemeOptions(
    val foreground: Color,
    val background: Color,
    val differYear: Boolean,
    val yearColor: Color,
) {
    companion object {
        val defaultLight
            get() = ClockThemeOptions(
                foreground = Color(0xff000000),
                background = Color(0xffffffff),
                differYear = true,
                yearColor = Color(0xffff0000),
            )
        val defaultDark
            get() = ClockThemeOptions(
                foreground = Color(0xffffffff),
                background = Color(0xff333333),
                differYear = true,
                yearColor = Color(0xffff0000),
            )
    }
}