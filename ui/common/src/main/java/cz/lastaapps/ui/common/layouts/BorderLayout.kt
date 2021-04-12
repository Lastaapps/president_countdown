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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

//testing
@Preview
@Composable
private fun Preview() {
    BorderLayout(
        //modifier = Modifier.size(128.dp).padding(8.dp),
        //modifier = Modifier.size(512.dp).padding(8.dp),
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
            .padding(8.dp),

        topContent = { Box(
            Modifier
                .size(80.dp, 20.dp)
                .background(Color.Red)) },
        bottomContent = { Box(
            Modifier
                .size(20.dp, 30.dp)
                .background(Color.Green)) },
        startContent = { Box(
            Modifier
                .size(30.dp, 40.dp)
                .background(Color.Yellow)) },
        endContent = { Box(
            Modifier
                .size(50.dp, 160.dp)
                .background(Color.Blue)) },
        centerContent = { Box(
            Modifier
                .size(60.dp, 70.dp)
                .background(Color.Magenta)) },
        spaceBy = 0.dp
    )
}

@Composable
fun BorderLayout(
    modifier: Modifier = Modifier,
    topContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
    startContent: (@Composable () -> Unit)? = null,
    endContent: (@Composable () -> Unit)? = null,
    centerContent: (@Composable () -> Unit)? = null,
    spaceBy: Dp = 0.dp,
) {
    ConstraintLayout(modifier = modifier) {

        val (topConst, bottomConst, startConst, endConst, centerConst) = createRefs()

        Box(
            modifier = Modifier.constrainAs(topConst) {
                top.linkTo(parent.top)
                start.linkTo(startConst.end, spaceBy)
                end.linkTo(endConst.start, spaceBy)

                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
            contentAlignment = Alignment.Center,
        ) {
            topContent?.let { it() }
        }

        Box(
            modifier = Modifier.constrainAs(bottomConst) {
                bottom.linkTo(parent.bottom)
                start.linkTo(startConst.end, spaceBy)
                end.linkTo(endConst.start, spaceBy)

                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
            contentAlignment = Alignment.Center,
        ) {
            bottomContent?.let { it() }
        }

        Box(
            modifier = Modifier.constrainAs(startConst) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)

                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
            contentAlignment = Alignment.Center,
        ) {
            startContent?.let { it() }
        }

        Box(
            modifier = Modifier.constrainAs(endConst) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)

                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
            contentAlignment = Alignment.Center,
        ) {
            endContent?.let { it() }
        }

        Box(
            modifier = Modifier.constrainAs(centerConst) {
                top.linkTo(topConst.bottom, spaceBy)
                bottom.linkTo(bottomConst.top, spaceBy)
                start.linkTo(startConst.end, spaceBy)
                end.linkTo(endConst.start, spaceBy)

                width = Dimension.preferredWrapContent
                height = Dimension.preferredWrapContent
            },
            contentAlignment = Alignment.Center,
        ) {
            centerContent?.let { it() }
        }
    }
}
