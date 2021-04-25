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

package cz.lastaapps.president.wallpaper.settings.files

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import cz.lastaapps.president.core.coroutines.loopingStateFlow
import cz.lastaapps.president.wallpaper.R
import cz.lastaapps.ui.common.extencions.LocalActivity

/**
 * Explains why storage permission is required and lets user grant it
 * */
@Composable
internal fun RequestStoragePermissionDialog(
    shown: Boolean,
    onShownChanged: (Boolean) -> Unit,
) {
    if (shown) {
        val activity = LocalActivity.requireActivity()

        val scope = rememberCoroutineScope()
        val showSystemDialog by remember(scope, activity) {
            loopingStateFlow(scope) { canAskForStoragePermission(activity) }
        }.collectAsState()

        AlertDialog(
            onDismissRequest = { onShownChanged(false) },
            title = {
                Text(stringResource(id = R.string.perm_title))
            },
            text = {
                Text(text = stringResource(id = R.string.perm_text))
            },
            confirmButton = {
                if (showSystemDialog) {
                    Button(onClick = {
                        requestStoragePermission(activity)

                        onShownChanged(false)
                    }) {
                        Text(stringResource(id = R.string.perm_system))
                    }
                } else {
                    Button(onClick = {

                        openPermissionsSettings(activity)

                        onShownChanged(false)
                    }) {
                        Text(text = stringResource(id = R.string.perm_setting))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onShownChanged(false)
                }) {
                    Text(text = stringResource(id = R.string.perm_cancel))
                }
            },
        )
    }
}
