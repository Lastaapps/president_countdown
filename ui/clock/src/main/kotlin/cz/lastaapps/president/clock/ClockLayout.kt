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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.ui.common.layouts.BottomLimitingLayout

private val paddingVertical = 24.dp

/**
 * Layout with the clock in the top and content in the bottom
 * */
@Composable
fun ClockLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        val (clockConst, contentConst) = createRefs()

        CreateClock(
            modifier = Modifier.constrainAs(clockConst) {
                top.linkTo(parent.top, paddingVertical)
                centerHorizontallyTo(parent)
            }
        )

        val scrollState = rememberScrollState()

        val contentModifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
            .constrainAs(contentConst) {
                centerHorizontallyTo(parent)
                top.linkTo(clockConst.bottom, paddingVertical)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }

        BottomLimitingLayout(contentModifier) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxSize(),
            ) {
                content()
            }
        }
    }
}

