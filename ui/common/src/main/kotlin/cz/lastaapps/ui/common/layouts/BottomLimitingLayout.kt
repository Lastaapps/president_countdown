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

package cz.lastaapps.ui.common.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min


@Composable
fun WidthLimitingLayout(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    maxWidth: Dp = 512.dp,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {

        val childWidth = min(maxWidth, this.maxWidth)

        Box(
            modifier = Modifier.width(childWidth),
            contentAlignment = contentAlignment,
        ) {
            content()
        }
    }
}

@Composable
fun BottomLimitingLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = WidthLimitingLayout(
    modifier = modifier,
    contentAlignment = Alignment.BottomCenter,
    content = content,
)

