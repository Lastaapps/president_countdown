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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import kotlin.math.roundToInt


@Composable
internal fun HexField(
    color: Color,
    onColorChanged: (Color) -> Unit,
    modifier: Modifier = Modifier,
    alphaEnabled: Boolean = true,
) {
    var text by rememberMutableSaveable(color) { mutableStateOf(color.toHexColor(alphaEnabled)) }

    TextField(
        value = text,
        onValueChange = {
            text = it
            val parsed = parseHexColor(it, alphaEnabled) ?: return@TextField
            onColorChanged(parsed)
        },
        singleLine = true,
        label = { Text(stringResource(id = R.string.label_hex)) },
        leadingIcon = { Text(text = "#") },
        modifier = modifier.width(((if (alphaEnabled) 8 else 6) * 24).dp),
    )
}

@SuppressLint("DefaultLocale")
private fun Color.toHexColor(includeAlpha: Boolean = true): String {
    var text = ""

    //for the numbers < 0x10 zero isn't shown
    fun String.fixMissingZero(): String =
        if (length == 1) "0$this" else this

    if (includeAlpha)
        text += (alpha * 255).roundToInt().toString(16).fixMissingZero()

    text += (red * 255).roundToInt().toString(16).fixMissingZero()
    text += (green * 255).roundToInt().toString(16).fixMissingZero()
    text += (blue * 255).roundToInt().toString(16).fixMissingZero()

    return text.toUpperCase()
}

private fun parseHexColor(
    hex: String,
    alphaEnabled: Boolean = true,
    acceptShort: Boolean = false
): Color? {

    //removes #
    @Suppress("NAME_SHADOWING")
    var hex = hex.filter { it != '#' }

    if (acceptShort) {
        //converts #ABC -> #AABBCC or #ABCD -> #AABBCCDD
        if (hex.length in listOf(3, 4)) {
            var completeHex = ""
            hex.forEach { completeHex += "$it$it" }
            hex = completeHex
        }
    }

    if (alphaEnabled) {
        if (hex.length != 8) return null
    } else {
        if (hex.length != 6) return null
        hex = "ff$hex"
    }

    return try {
        Color(hex.toLong(16))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}