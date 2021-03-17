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

package cz.lastaapps.president.privacypolicy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import cz.lastaapps.common.Communication
import cz.lastaapps.president.constants.githubProjectName
import cz.lastaapps.ui.common.components.GeneralDialog
import cz.lastaapps.ui.common.components.ImageTextRow
import cz.lastaapps.ui.common.extencions.LocalActivity

/**Asks user to agree the privacy policy*/
@Composable
internal fun PolicyDialog(
    shown: Boolean,
    stateChanged: (Boolean) -> Unit,
) {
    if (shown) {
        GeneralDialog(
            onDismissRequest = { stateChanged(false) },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
            content = {
                Content(
                    requestDismiss = { stateChanged(false) }
                )
            },
            buttons = {},
        )
    }
}

@Composable
private fun Content(requestDismiss: () -> Unit, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val activity = LocalActivity.getCompat()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {

        val typo = MaterialTheme.typography
        val paddingModifier = Modifier.padding(4.dp)

        Text(
            text = stringResource(id = R.string.dialog_title),
            style = typo.h5,
            textAlign = TextAlign.Center,
            modifier = paddingModifier,
        )
        Text(
            text = stringResource(id = cz.lastaapps.president.core.R.string.app_name),
            style = typo.h6,
            textAlign = TextAlign.Center,
            modifier = paddingModifier,
        )
        Text(
            text = stringResource(id = R.string.dialog_license),
            style = typo.caption,
            textAlign = TextAlign.Center,
            modifier = paddingModifier,
        )
        Text(
            text = stringResource(id = R.string.dialog_nutshell),
            style = typo.body2,
            textAlign = TextAlign.Center,
            modifier = paddingModifier,
        )

        Divider(
            modifier = paddingModifier,
        )

        OutlinedButton(
            onClick = {
                PrivacyPolicy.showPolicies(context)
            },
            modifier = paddingModifier,
        ) {
            Text(text = stringResource(id = R.string.dialog_read_button))
        }

        var agreed by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                agreed = !agreed
            }
        ) {
            Checkbox(
                checked = agreed,
                onCheckedChange = { agreed = it },
                modifier = paddingModifier,
            )
            Text(
                text = stringResource(id = R.string.dialog_have_read),
                modifier = paddingModifier,
            )
        }

        Divider(
            modifier = paddingModifier,
        )

        Button(
            onClick = {
                PolicyManager(context).userAgreed()
                requestDismiss()
            },
            enabled = agreed,
            modifier = paddingModifier,
        ) {
            Text(text = stringResource(id = R.string.dialog_agree))
        }

        Button(
            onClick = {
                Communication.openProjectsGithub(context, githubProjectName)
            },
            modifier = paddingModifier,
        ) {
            ImageTextRow(
                painter = painterResource(id = cz.lastaapps.common.R.drawable.ic_github),
                text = stringResource(id = R.string.dialog_show_source),
                contentDescription = stringResource(id = cz.lastaapps.common.R.string.content_description_github_project)
            )
        }

        if (activity != null) {
            TextButton(
                onClick = {
                    activity.finish()
                },
                modifier = paddingModifier,
            ) {
                Text(text = stringResource(id = R.string.dialog_disagree))
            }
        }
    }
}

