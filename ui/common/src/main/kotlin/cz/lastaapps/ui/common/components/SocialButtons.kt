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

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cz.lastaapps.ui.common.extencions.iconSize


@Composable
fun SocialButton(
    onClick: (Context) -> Unit,
    content: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    IconButton(onClick = { onClick(context) }, modifier = modifier) {
        content(Modifier.size(iconSize))
    }
}

@Composable
fun socialIconContent(vector: ImageVector, @StringRes contentId: Int) =
    @Composable { modifier: Modifier ->
        Icon(
            vector,
            contentDescription = stringResource(id = contentId),
            modifier = modifier
        )
    }

@Composable
fun socialImageContent(@DrawableRes id: Int, @StringRes contentId: Int) =
    @Composable { modifier: Modifier ->
        Image(
            painterResource(id = id),
            contentDescription = stringResource(id = contentId),
            modifier = modifier
        )
    }
