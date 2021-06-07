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

package cz.lastaapps.president.wallpaper.settings.help

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import cz.lastaapps.president.wallpaper.R
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable

@Composable
internal fun WallpaperHelpIcon(modifier: Modifier = Modifier) {

    var shown by rememberMutableSaveable {
        mutableStateOf(!HelpShownStorage.shown)
    }

    IconButton(onClick = { shown = !shown }, modifier = modifier) {
        Icon(
            Icons.Default.Help,
            contentDescription = stringResource(id = R.string.help_content_description)
        )
    }

    WallpaperHelpDialog(shown = shown, onDismissRequest = { shown = false })
}

@Composable
internal fun WallpaperHelpDialog(shown: Boolean, onDismissRequest: () -> Unit) {
    if (shown) {

        val myDismiss = {
            onDismissRequest()
            HelpShownStorage.shown = true
        }

        AlertDialog(
            onDismissRequest = myDismiss,
            title = {
                Text(stringResource(id = R.string.help_title))
            },
            text = {
                Text(stringResource(id = R.string.help_text))
            },
            confirmButton = {
                Button(onClick = { myDismiss() }) {
                    Text(
                        text = stringResource(id = R.string.help_ok),
                        textAlign = TextAlign.Center,
                    )
                }
            },
        )
    }
}
        

