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

package cz.lastaapps.ui.colorpicker

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.Test


class ColorPickerTest {

    @Test
    fun toHexColor() {

        val inputs = listOf(0xffaabbcc).map { Color(it) }
        val results = listOf("ffaabbcc")

        for (i in inputs.indices) {
            assertThat(inputs[i].toHexColor().toLowerCase()).isEqualTo(results[i])
        }
    }

    @Test
    fun parseHexColor() {

        listOf(
            "#ffaabbcc" to 0xffaabbcc,
            "#aabbcc" to 0xffaabbcc,
            "#abc" to 0xffaabbcc,
            "#abcd" to 0xaabbccdd,
            "#abcde" to null,
            "#fdsafjklů" to null,
            "#fffffffff" to null,
        ).map {
            it.first to it.second?.let { color -> Color(color) }
        }.forEach {
            assertThat(parseHexColor(it.first)).isEqualTo(it.second)
        }
    }
}