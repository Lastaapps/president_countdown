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

package cz.lastaapps.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


private const val DividerAlpha = 0.12f

/**
 * A divider is a thin line that groups content in lists and layouts
 *
 * @param color color of the divider line
 * @param thickness thickness of the divider line, 1 dp is used by default
 * @param startIndent start offset of this line, no offset by default
 */
@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    val indentMod = if (startIndent.value != 0f) {
        Modifier.padding(top = startIndent)
    } else {
        Modifier
    }
    Box(
        modifier
            .then(indentMod)
            .fillMaxHeight()
            .width(thickness)
            .background(color = color)
    )
}
