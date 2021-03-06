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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import kotlin.math.min

object ColorPreviewConstants {
    val size = 40.dp
}

/**
 * Shows a circle with color given
 * */
@Composable
fun ColorPreview(
    color: Color,
    modifier: Modifier = Modifier,
    preferredSize: Dp = ColorPreviewConstants.size,
) {
    val strokeColor = MaterialTheme.colors.onSurface

    BoxWithConstraints(
        modifier = modifier.sizeIn(
            maxWidth = preferredSize,
            maxHeight = preferredSize
        )
    ) {

        val rectangleSize = min(maxWidth, maxHeight)

        Canvas(modifier = Modifier.size(rectangleSize, rectangleSize)) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val drawingArea = min(canvasWidth, canvasHeight)
            val radius = 0.9f * drawingArea / 2

            drawCircle(color, radius, style = Fill)
            drawCircle(strokeColor, radius, style = Stroke(width = drawingArea / 20))
        }
    }
}

