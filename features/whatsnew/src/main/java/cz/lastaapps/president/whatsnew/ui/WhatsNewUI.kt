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

package cz.lastaapps.president.whatsnew.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cz.lastaapps.common.Communication
import cz.lastaapps.president.constants.githubProjectName
import cz.lastaapps.president.whatsnew.BuildConfig
import cz.lastaapps.president.whatsnew.R
import cz.lastaapps.president.whatsnew.WhatsNewProperties
import cz.lastaapps.president.whatsnew.assets.*
import cz.lastaapps.ui.common.components.ImageTextRow
import cz.lastaapps.ui.settings.SwitchSettings
import java.time.format.DateTimeFormatter

@Composable
fun WhatsNewDialog(
    shown: Boolean,
    visibilityChanged: (Boolean) -> Unit,
) {
    if (shown) {
        Dialog(
            onDismissRequest = { visibilityChanged(false) }
        ) {

            //invisible, sets the max size for the content
            Box(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .fillMaxHeight(.9f),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colors.surface,
                ) {
                    Content(visibilityChanged, Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
private fun Content(
    visibilityChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.animateContentSize()) {

        val (topConst, centerConst, bottomConst) = createRefs()

        val paddingModifier = Modifier.padding(top = 4.dp, bottom = 4.dp)

        Text(
            text = stringResource(id = R.string.whatsnew_dialog_title),
            modifier = paddingModifier.constrainAs(topConst) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top)
            },
            style = MaterialTheme.typography.h5,
        )

        val scroll = rememberScrollState()
        VersionsContent(
            modifier = paddingModifier
                .verticalScroll(scroll)
                .constrainAs(centerConst) {
                    centerHorizontallyTo(parent)
                    top.linkTo(topConst.bottom)
                    bottom.linkTo(bottomConst.top)
                    height = Dimension.preferredWrapContent
                })

        Column(
            modifier = Modifier.constrainAs(bottomConst) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom)
            }
        ) {
            ViewCommits(paddingModifier.fillMaxWidth())

            AutoLaunchSettings(
                modifier = paddingModifier.fillMaxWidth()
            )

            TextButton(
                onClick = { visibilityChanged(false) },
                modifier = paddingModifier.align(Alignment.End),
            ) {
                Text(stringResource(id = R.string.whatsnew_dialog_ok))
            }
        }
    }
}

@Composable
private fun ViewCommits(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    Button(
        onClick = { Communication.openProjectsCommits(context, githubProjectName) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
    ) {
        ImageTextRow(
            painter = painterResource(id = cz.lastaapps.common.R.drawable.ic_github),
            text = stringResource(id = R.string.view_commits),
        )
    }
}

@Composable
private fun AutoLaunchSettings(modifier: Modifier = Modifier) {

    var settings: WhatsNewProperties? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect("") {
        settings = WhatsNewProperties.getInstance(context, scope).also {
            it.updateVersion()
        }
    }

    settings?.let {

        val state by it.autoLaunchFlow.collectAsState()

        SwitchSettings(
            text = stringResource(id = R.string.sett_auto_launch),
            checked = state,
            onChange = { it.autoLaunch = !state },
            modifier = modifier,
        )
    }
}

@Composable
private fun VersionsContent(modifier: Modifier = Modifier) {

    val versions = loadVersions()

    Column(modifier) {
        for (version in versions) {
            VersionItem(
                version,
                Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun VersionItem(version: Version, modifier: Modifier = Modifier) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(text = version.name + " - " + version.buildNumber.toString())
            Text(text = version.releasedDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
            Divider()
            Text(
                text = version.getLocalizedContent(LocalContext.current),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Loads version infos form storage
 * */
@Composable
private fun loadVersions(): List<Version> {

    var list: List<Version> by remember { mutableStateOf(ArrayList()) }
    val context = LocalContext.current

    LaunchedEffect("") {
        list = Loader(context).load().let {
            when {
                BuildConfig.isAlpha -> it.filterAlpha()
                BuildConfig.isBeta -> it.filterBeta()
                else -> it.filterGeneral()
            }
        }
    }

    return list
}
