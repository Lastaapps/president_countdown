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

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cz.lastaapps.common.Communication
import cz.lastaapps.president.constants.githubProjectName
import cz.lastaapps.ui.common.components.SocialButton
import cz.lastaapps.ui.common.components.socialIconContent
import cz.lastaapps.ui.common.components.socialImageContent

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
        Row(horizontalArrangement = Arrangement.Center) {
            Web()
            Facebook()
            Github()
            OtherApps()
            Telegram()
        }
    }
}

//TODO remove after the end of the mandate of Miloš Zeman
@Composable
private fun Web(modifier: Modifier = Modifier) {
    SocialButton(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.zemancountdown.cz")
            it.startActivity(intent)
        },
        socialIconContent(
            vector = Icons.Default.Language,
            contentId = R.string.content_description_web,
        ),
        modifier = modifier
    )
}

@Composable
private fun Facebook(modifier: Modifier = Modifier) {
    SocialButton(
        onClick = { Communication.openFacebook(it) },
        socialImageContent(
            id = cz.lastaapps.common.R.drawable.ic_facebook,
            contentId = cz.lastaapps.common.R.string.content_description_facebook,
        ),
        modifier = modifier
    )
}

@Composable
private fun Github(modifier: Modifier = Modifier) {
    SocialButton(
        onClick = { Communication.openProjectsGithub(it, githubProjectName) },
        socialImageContent(
            id = cz.lastaapps.common.R.drawable.ic_github,
            contentId = cz.lastaapps.common.R.string.content_description_github_project,
        ),
        modifier = modifier
    )
}

@Composable
private fun OtherApps(modifier: Modifier = Modifier) {
    SocialButton(
        onClick = { Communication.openPlayStore(it) },
        socialImageContent(
            id = cz.lastaapps.common.R.drawable.ic_play_store,
            contentId = cz.lastaapps.common.R.string.content_description_play_store,
        ),
        modifier = modifier
    )
}

@Composable
private fun Telegram(modifier: Modifier = Modifier) {
    SocialButton(
        onClick = { Communication.openTelegram(it) },
        socialImageContent(
            id = cz.lastaapps.common.R.drawable.ic_telegram,
            contentId = cz.lastaapps.common.R.string.content_description_telegram,
        ),
        modifier = modifier
    )
}