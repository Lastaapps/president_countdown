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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import cz.lastaapps.ui.common.components.ExpandSwitchIconButton
import cz.lastaapps.ui.common.components.IconTextRow
import cz.lastaapps.ui.common.components.ImageTextRow
import cz.lastaapps.ui.common.extencions.iconSize

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandingIcons(
    label: String,
    items: List<LabelPainterActionData>,
    expanded: Boolean,
    onExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    otherIcons: (@Composable () -> Unit)? = null,
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .padding(start = 8.dp, end = 8.dp)
            .then(modifier)
            .animateContentSize(),
    ) {

        val (textConst, iconsConst, contentConst) = createRefs()

        val barrier = createBottomBarrier(textConst, iconsConst)

        Box(
            modifier = Modifier.constrainAs(textConst) {
                start.linkTo(parent.start)
                end.linkTo(iconsConst.start, 8.dp)
                top.linkTo(parent.top)
                bottom.linkTo(barrier)
            },
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(label)
        }


        Row(
            modifier = Modifier.constrainAs(iconsConst) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(barrier)
            },
            horizontalArrangement = Arrangement.End
        ) {
            ExpandSwitchIconButton(
                expanded = expanded,
                onExpanded = onExpanded,
            )

            otherIcons?.let {
                it()
            }
        }

        IconsContent(
            items = items,
            expanded = expanded,
            modifier = Modifier.constrainAs(contentConst) {
                top.linkTo(barrier)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun IconsContent(
    items: List<LabelPainterActionData>,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.TopCenter) {
        AnimatedVisibility(
            !expanded,
            enter = fadeIn() + slideInVertically({ it * -1 }),
            exit = fadeOut() + slideOutVertically({ it * -1 }),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                items.forEach { item ->
                    if (item.isIcon)
                        IconButton(onClick = item.action) {
                            Icon(
                                painter = item.painter,
                                contentDescription = item.contentDescription,
                            )
                        }
                    else
                        IconButton(onClick = item.action) {
                            Image(
                                painter = item.painter,
                                contentDescription = item.contentDescription,
                                modifier = Modifier.size(iconSize),
                            )
                        }
                }
            }
        }
        AnimatedVisibility(
            expanded,
            enter = fadeIn() + slideInVertically({ it * -1 }),
            exit = fadeOut()// + slideOutVertically({it * -1}),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.width(IntrinsicSize.Max),
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .clickable(onClick = item.action)
                            .padding(4.dp)
                            .fillMaxWidth(),
                    ) {

                        if (item.isIcon)
                            IconTextRow(
                                painter = item.painter,
                                text = item.label,
                                contentDescription = item.contentDescription,
                            )
                        else
                            ImageTextRow(
                                painter = item.painter,
                                text = item.label,
                                contentDescription = item.contentDescription,
                            )
                    }
                }
            }
        }
    }
}

data class LabelPainterActionData(
    val isIcon: Boolean, //or image
    val painter: Painter,
    val label: String,
    val contentDescription: String = label,
    val action: () -> Unit,
) {
    companion object {
        @Composable
        fun fromResources(
            isIcon: Boolean,
            @DrawableRes imageId: Int,
            @StringRes textId: Int,
            @StringRes contentId: Int,
            action: () -> Unit
        ) = LabelPainterActionData(
            isIcon = isIcon,
            painter = painterResource(id = imageId),
            label = stringResource(id = textId),
            contentDescription = stringResource(id = contentId),
            action = action,
        )
    }
}
