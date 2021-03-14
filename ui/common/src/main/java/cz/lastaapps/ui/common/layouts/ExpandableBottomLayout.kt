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

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.lastaapps.ui.common.R
import cz.lastaapps.ui.common.extencions.iconSize

//TODO redo with MotionLayout
@ExperimentalAnimationApi
@Composable
fun ExpandableBottomLayout(
    expanded: Boolean,
    onExpanded: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier,
    ) {
        BottomLimitingLayout {

            //Switch icon
            @Composable
            fun switch() {
                IconButton(
                    onClick = { onExpanded(!expanded) },
                ) {
                    Icon(
                        if (expanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                        contentDescription = stringResource(
                            if (expanded)
                                R.string.content_description_hide
                            else
                                R.string.content_description_show
                        ),
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            //singe switch
            AnimatedVisibility(
                visible = !expanded,
                enter = slideInVertically({ it * 3 / 2 * -1 }) + fadeIn(),
                exit = slideOutVertically({ it * 3 / 2 * -1 }) + fadeOut(),
            ) {
                switch()
            }

            //Content + switch
            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically({ it * 3 / 2 }) + fadeIn(),
                exit = slideOutVertically({ it * 3 / 2 }) + fadeOut(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {

                    switch()

                    content()
                }
            }
        }
    }
}

