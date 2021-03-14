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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.ui.common.layouts.WidthLimitingLayout


@Composable
fun GeneralDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    title: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
    buttons: @Composable (() -> Unit)? = null,
    maxWidth: Float = .9f,
    maxHeight: Float = .9f,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        //invisible, sets the max size for the content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable(
                    onClick = {
                        if (properties.dismissOnClickOutside)
                            onDismissRequest()
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ),
            contentAlignment = Alignment.Center,
        ) {
            WidthLimitingLayout(
                modifier = Modifier
                    .fillMaxWidth(maxWidth)
                    .fillMaxHeight(maxHeight),
                contentAlignment = Alignment.Center,
            ) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {},
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colors.surface,
                ) {
                    GeneralDialogContent(
                        title = title,
                        content = content,
                        buttons = buttons,
                    )
                }
            }
        }
    }
}

@Composable
private fun GeneralDialogContent(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
    buttons: @Composable (() -> Unit)? = null,
) {
    ConstraintLayout(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        val (topConst, centerConst, bottomConst) = createRefs()

        val paddingModifier = Modifier.padding(top = 4.dp, bottom = 4.dp)

        Box(modifier = paddingModifier.constrainAs(topConst) {
            centerHorizontallyTo(parent)
            top.linkTo(parent.top)

            width = Dimension.fillToConstraints
            height = Dimension.wrapContent
        }) {
            title?.let {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    val textStyle = MaterialTheme.typography.subtitle1
                    ProvideTextStyle(textStyle, title)
                }
            }
        }

        val scroll = rememberScrollState()
        Box(
            paddingModifier
                .verticalScroll(scroll)
                .constrainAs(centerConst) {
                    centerHorizontallyTo(parent)
                    top.linkTo(topConst.bottom)
                    bottom.linkTo(bottomConst.top)

                    width = Dimension.fillToConstraints
                    height = Dimension.preferredWrapContent
                }
        ) {
            if (content != null)
                content()
        }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.constrainAs(bottomConst) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom)

                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
        ) {
            if (buttons != null)
                buttons()
        }
    }
}


