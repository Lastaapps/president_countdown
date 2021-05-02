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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * Text with image
 * */
@Composable
fun ImageTextRow(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String = text,
    iconSize: Dp = cz.lastaapps.ui.common.extencions.iconSize,
    textStyle: TextStyle = TextStyle.Default,
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    Image(painter = painter, contentDescription, modifier = Modifier.size(iconSize))
    Text(text, style = textStyle, modifier = Modifier.padding(start = 8.dp))
}

/**
 * Text with icon
 * */
@Composable
fun IconTextRow(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String = text,
    iconSize: Dp = cz.lastaapps.ui.common.extencions.iconSize,
    textStyle: TextStyle = TextStyle.Default,
) = Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    Icon(painter, contentDescription, modifier = Modifier.size(iconSize))
    Text(text, style = textStyle, modifier = Modifier.padding(start = 8.dp))
}

@Composable
fun IconTextRow(
    vector: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    contentDescription: String = text,
    iconSize: Dp = cz.lastaapps.ui.common.extencions.iconSize,
    textStyle: TextStyle = TextStyle.Default,
) = IconTextRow(
    painter = rememberVectorPainter(vector),
    text = text,
    modifier = modifier,
    contentDescription = contentDescription,
    iconSize = iconSize,
    textStyle = textStyle,
)
