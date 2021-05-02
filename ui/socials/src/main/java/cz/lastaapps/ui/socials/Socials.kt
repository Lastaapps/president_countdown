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

package cz.lastaapps.ui.socials

import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cz.lastaapps.common.Communication
import cz.lastaapps.president.constants.githubProjectName
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.layouts.ExpandingIcons
import cz.lastaapps.ui.common.layouts.LabelPainterActionData
import cz.lastaapps.common.R as CommR

/**
 * Shows icons with links to Lasta apps socials
 * */
@Composable
fun Socials(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.secondaryVariant
) {
    Card(
        modifier = modifier,
        backgroundColor = backgroundColor
    ) {

        var expanded by rememberMutableSaveable { mutableStateOf(false) }

        val context = LocalContext.current
        val items = listOf(
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = CommR.drawable.ic_facebook,
                textId = CommR.string.facebook,
                contentId = CommR.string.content_description_facebook,
            ) {
                Communication.openProjectsGithub(context, githubProjectName)
            },
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = CommR.drawable.ic_github,
                textId = CommR.string.github_project,
                contentId = CommR.string.content_description_github_project,
            ) {
                Communication.openProjectsGithub(context, githubProjectName)
            },
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = CommR.drawable.ic_play_store,
                textId = CommR.string.play_store,
                contentId = CommR.string.content_description_play_store,
            ) {
                Communication.openPlayStore(context)
            },
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = CommR.drawable.ic_telegram,
                textId = CommR.string.telegram,
                contentId = CommR.string.content_description_telegram,
            ) {
                Communication.openTelegram(context)
            },
        )

        ExpandingIcons(
            label = stringResource(id = R.string.socials_label),
            items = items,
            expanded = expanded,
            onExpanded = { expanded = !expanded }
        )

        return@Card
    }
}