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

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.min
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.ui.common.themes.extended

@Composable
fun BlinkingIndicator(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier) {

        val (contentConst, canvasConst) = createRefs()

        if (enabled) {
            BoxWithConstraints(
                modifier = Modifier.constrainAs(canvasConst) {
                    centerVerticallyTo(contentConst)
                    centerHorizontallyTo(contentConst)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                contentAlignment = Alignment.Center
            ) {
                val size = min(maxWidth, maxHeight)

                val animationDuration = 500

                val infiniteTransition = rememberInfiniteTransition()
                val progress by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = animationDuration
                            0.7f at animationDuration / 2
                        },
                        repeatMode = RepeatMode.Reverse
                    )
                )

                val color = MaterialTheme.extended.waring

                Canvas(modifier = Modifier.size(size)) {
                    val radius = size.roundToPx().toFloat() / 2

                    drawCircle(color, radius * progress, center)

                }
            }
        }

        Box(
            modifier = Modifier.constrainAs(contentConst) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)

                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
        ) {
            content()
        }
    }
}

