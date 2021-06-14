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

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

/*
* Styles for Clock
* */

private val redDigitLight = Color(0xffff0000)
private val redDigitDark = Color(0xffff0000)

private val digitSize = 28.sp
private val unitSize = 13.sp

internal val redDigitColor: Color
    @Composable
    get() = if (MaterialTheme.colors.isLight) redDigitLight else redDigitDark

internal val digitTextStyle
    @Composable
    get() = LocalTextStyle.current.copy(fontSize = digitSize)

internal val unitFonts
    @Composable
    get() = MaterialTheme.typography.caption.copy(fontSize = unitSize)
