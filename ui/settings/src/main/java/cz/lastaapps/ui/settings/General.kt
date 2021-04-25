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

package cz.lastaapps.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.ui.common.components.VerticalDivider
import cz.lastaapps.ui.common.themes.extended

val defaultSettingsActionWidth = 48.dp

/**
 * Text and switch
 * */
@Composable
fun SwitchSettings(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    useDivider: Boolean = false,
    description: String? = null,
    onChange: () -> Unit,
) {
    CustomSettings(
        title = text,
        onClick = onChange,
        modifier = modifier,
        useDivider = useDivider,
        description = description,
    ) {
        Switch(checked = checked, onCheckedChange = { onChange() })
    }
}

/**
 * Common alignment for settings components
 * */
@Composable
fun CustomSettings(
    title: String,
    useDivider: Boolean,
    modifier: Modifier = Modifier,
    description: String? = null,
    selected: String? = null,
    onClick: () -> Unit = {},
    actionWidth: Dp? = if (useDivider) defaultSettingsActionWidth else null,
    content: (@Composable () -> Unit)? = null,
) {
    CustomSettings(
        title = { SettingsTitle(title) },
        description = description?.let { { SettingsDescription(text = description) } },
        selected = selected?.let { { SettingsSelected(text = selected) } },
        useDivider = useDivider,
        modifier = modifier,
        onClick = onClick,
        actionWidth = actionWidth,
        content = content,
    )
}

/**
 * Common alignment for settings components
 * */
@Composable
fun CustomSettings(
    title: (@Composable () -> Unit),
    useDivider: Boolean,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    selected: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
    actionWidth: Dp? = if (useDivider) defaultSettingsActionWidth else null,
    content: (@Composable () -> Unit)? = null,
) {
    ConstraintLayout(
        modifier = modifier.clickable(onClick = onClick)
    ) {

        val padding = 8.dp

        val (textConst, dividerConst, contentConst) = createRefs()

        Column(
            modifier = Modifier.constrainAs(textConst) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
                end.linkTo(dividerConst.start, padding)

                width = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            title()

            description?.let {
                it()
            }

            selected?.let {
                it()
            }
        }

        if (content != null) {

            Box(
                modifier = Modifier.constrainAs(dividerConst) {
                    end.linkTo(contentConst.start, padding)
                    centerVerticallyTo(parent)

                    height = Dimension.value(32.dp)
                },
            ) {
                if (useDivider) {
                    VerticalDivider()
                }
            }

            Box(
                modifier = Modifier.constrainAs(contentConst) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)

                    width = if (actionWidth != null)
                        Dimension.value(actionWidth)
                    else
                        Dimension.wrapContent
                },
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}

/**
 * The title in custom settings
 * */
@Composable
fun SettingsTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier,
    )
}

/**
 * The description in custom settings
 * */
@Composable
fun SettingsDescription(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.extended.onSurfaceSecondary,
        modifier = modifier,
    )
}

/**
 * The selected option in custom settings
 * */
@Composable
fun SettingsSelected(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.secondary,
        modifier = modifier,
    )
}

/**
 * Background for settings components
 * */
@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    categoryName: String? = null,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = MaterialTheme.colors.surface.copy(alpha = LocalContentAlpha.current),
        border = border,
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            categoryName?.let {
                SettingsCategoryName(text = it)
            }
            content()
        }
    }
}

/**
 * Background for settings components
 * */
@Composable
fun SettingsGroupColumn(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    border: BorderStroke? = null,
    categoryName: String? = null,
    content: @Composable () -> Unit,
) {
    SettingsGroup(
        modifier = modifier,
        shape = shape,
        categoryName = categoryName,
        border = border,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            content()
        }
    }
}

/**
 * Name for a settings group
 * */
@Composable
fun SettingsCategoryName(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.secondary,
        modifier = modifier,
    )
}
