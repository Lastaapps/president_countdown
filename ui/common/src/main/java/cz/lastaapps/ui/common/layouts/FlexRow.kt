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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import kotlin.math.max

/**
 * Like a normal row, but an overflow function is enabled - if you exceed row width, items continue
 * on the next line
 * */
//TODO equal items per row
@Composable
fun FlexRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    rowsArrangement: Arrangement.Vertical = Arrangement.Top,
    itemsAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit,
) {

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->

        val verticalSpacing = rowsArrangement.spacing.roundToPx()
        val horizontalSpacing = horizontalArrangement.spacing.roundToPx()

        //measures composes
        val placeables = measurables.map { it.measure(constraints) }

        fun rowWidth(list: List<Placeable>): Int =
            max(list.sumBy { it.width + horizontalSpacing } - horizontalSpacing, 0)

        fun rowHeight(list: List<Placeable>): Int = list.maxOfOrNull { it.height } ?: 0


        val lines = mutableListOf<MutableList<Placeable>>()

        var addedLine = 0
        for (placable in placeables) {
            //add a new line if needed
            if (lines.getOrNull(addedLine) == null) {
                lines.add(mutableListOf())
            }

            val items = lines[addedLine]
            val rowWidth = rowWidth(items)
            //check if the placable can be put into this line or the next one
            if (items.isEmpty() || rowWidth + placable.width + horizontalSpacing <= constraints.maxWidth) {
                items.add(placable)
            } else {
                //puts composable into the next row
                addedLine++
                lines.add(mutableListOf(placable))
            }
        }

        val linesWidths = lines.map { rowWidth(it) }
        val linesHeights = lines.map { rowHeight(it) }

        val linesWidth = (linesWidths.maxOrNull() ?: 0)
            .coerceIn(constraints.minWidth, constraints.maxWidth)
        val linesHeight = max(linesHeights.sumBy { it + verticalSpacing } - verticalSpacing, 0)
            .coerceIn(constraints.minHeight, constraints.maxHeight)

        //not tested with fillMax...()
        @Suppress("UnnecessaryVariable")
        val width =
            linesWidth //if (constraints.hasBoundedWidth) linesWidth else constraints.maxWidth

        @Suppress("UnnecessaryVariable")
        val height =
            linesHeight //if (constraints.hasBoundedHeight) linesHeight else constraints.maxHeight

        layout(width, height) {

            val rowsOffsets = IntArray(lines.size)
            with(rowsArrangement) {
                density.arrange(height, lines.map { rowHeight(it) }.toIntArray(), rowsOffsets)
            }

            lines.forEachIndexed { lineIndex, line ->
                val verticalOffset = rowsOffsets[lineIndex]
                val lineHeight = rowHeight(line)

                val horizontalOffsets = IntArray(line.size)
                with(horizontalArrangement) {
                    density.arrange(
                        width,
                        line.map { it.width }.toIntArray(),
                        layoutDirection,
                        horizontalOffsets
                    )
                }

                line.forEachIndexed { placableIndex, placeable ->

                    val itemOffset = itemsAlignment.align(placeable.height, lineHeight)
                    val horizontalOffset = horizontalOffsets[placableIndex]

                    placeable.place(horizontalOffset, verticalOffset + itemOffset)
                }
            }
        }
    }
}

