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

package cz.lastaapps.president.app.ui.idea

import android.content.Intent
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import cz.lastaapps.president.app.R
import cz.lastaapps.ui.common.extencions.viewModelKt

//TODO remove after Miloš Zeman
@Composable
internal fun IdeaDialog() {

    val viewModel = viewModelKt(modelClass = IdeaViewModel::class)

    if (viewModel.isReady.collectAsState().value) {

        var shown by remember {
            mutableStateOf(viewModel.ideaRepo.shouldShow())
        }

        if (shown) {

            val context = LocalContext.current

            LaunchedEffect(true) {
                viewModel.ideaRepo.shown()
            }

            AlertDialog(
                onDismissRequest = { shown = false },
                title = {
                    Text(stringResource(id = R.string.idea_dialog_title))
                },
                text = {
                    Text(stringResource(id = R.string.idea_dialog_message))
                },
                confirmButton = {
                    Button(onClick = { shown = false }) {
                        Text(stringResource(id = R.string.idea_dialog_ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, "http://zemancountdown.cz".toUri())
                        )
                    }) {
                        Text(stringResource(id = R.string.idea_dialog_web))
                    }
                }
            )
        }
    }
}