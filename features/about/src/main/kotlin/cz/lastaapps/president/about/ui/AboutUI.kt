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

package cz.lastaapps.president.about.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import cz.lastaapps.common.Communication
import cz.lastaapps.president.about.R
import cz.lastaapps.president.clock.ClockLayout
import cz.lastaapps.president.core.functionality.getLocale
import cz.lastaapps.president.core.functionality.getVersionCode
import cz.lastaapps.president.core.functionality.getVersionName
import cz.lastaapps.president.core.president.President
import cz.lastaapps.president.privacypolicy.PrivacyPolicy
import cz.lastaapps.president.whatsnew.ui.WhatsNewDialog
import cz.lastaapps.ui.common.components.*
import cz.lastaapps.ui.common.extencions.iconSize
import cz.lastaapps.ui.common.extencions.rememberMutableSaveable
import cz.lastaapps.ui.common.layouts.ExpandingIcons
import cz.lastaapps.ui.common.layouts.LabelPainterActionData
import cz.lastaapps.ui.socials.DeveloperNotice
import cz.lastaapps.ui.socials.Socials


/**
 * Shows info about the app and links to some important places
 * */
@Composable
fun About(modifier: Modifier = Modifier) {
    //puts the clock in the top
    ClockLayout(modifier) {
        Box(
            contentAlignment = Alignment.BottomCenter,
        ) {
            Content()
        }
    }
}

@Composable
private fun Content(modifier: Modifier = Modifier) {

    Card(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            modifier = Modifier.padding(16.dp)
        ) {

            AppName()

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Rate()
                Share()
            }

            //links to the info about President, definitely no Rickrolling
            PresidentWebLinks()
            Socials()

            //scrolls horizontally
            val moreScroll = rememberScrollState()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(moreScroll)
            ) {
                WhatsNew()
                ShowPrivacyPolicy()
                Licenses()
            }

            //describes, why the UI can seen buggy and some numbers are eventually skipped
            //disabled - app doesn't lag as much any more and this would drop down overall feel
            //BuggyUI()

            DeveloperNotice()
            Version()
        }
    }
}

@Composable
private fun AppName(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = cz.lastaapps.president.core.R.string.app_name),
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Composable
private fun Rate(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(onClick = { AboutActions.rateAction(context) }, modifier = modifier) {
        IconTextRow(
            Icons.Default.Star,
            text = stringResource(id = R.string.rate),
            iconSize = iconSize
        )
    }
}

@Composable
private fun Share(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(onClick = { AboutActions.shareAction(context) }, modifier = modifier) {
        IconTextRow(
            Icons.Default.Share,
            text = stringResource(id = R.string.share),
            iconSize = iconSize
        )
    }
}

@Composable
private fun PresidentWebLinks(modifier: Modifier = Modifier) {

    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.secondaryVariant,
    ) {
        var expanded by rememberMutableSaveable {
            mutableStateOf(false)
        }

        val context = LocalContext.current
        val items = listOf(

            //TODO remove after the end of the mandate of Miloš Zeman
            LabelPainterActionData(
                isIcon = true,
                painter = rememberVectorPainter(Icons.Default.Language),
                label = stringResource(id = R.string.web),
                contentDescription = stringResource(id = R.string.content_description_web)
            ) {
                openWeb(context, "https://www.zemancountdown.cz")
            },
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = R.drawable.wikipedia,
                textId = R.string.wikipedia,
                contentId = R.string.content_description_wikipedia
            ) {
                openWeb(context, President.getWikiLink(context.getLocale()))
            },
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = R.drawable.hrad_cz,
                textId = R.string.hrad,
                contentId = R.string.content_description_hrad
            ) {
                openWeb(context, "https://www.hrad.cz")
            },
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = R.drawable.je_milos_zeman_stale_nazivu,
                textId = R.string.jmzsn,
                contentId = R.string.content_description_jmzsn
            ) {
                Communication.openFacebookPage(
                    context,
                    "https://www.facebook.com/KancelarPrezidentaRepubliky/"
                )
            },
            LabelPainterActionData.fromResources(
                isIcon = false,
                imageId = R.drawable.je_milos_zeman_dobrym_prezidentem,
                textId = R.string.jmzdp,
                contentId = R.string.content_description_jmzdp
            ) {
                openWeb(
                    context,
                    "https://play.google.com/store/apps/details?id=cz.JMZDP.jemilozemandobrmprezidentem"
                )
            },
            LabelPainterActionData(
                isIcon = true,
                painter = rememberVectorPainter(Icons.Default.Lightbulb),
                label = stringResource(id = R.string.surprise),
                contentDescription = stringResource(id = R.string.content_description_surprise)
            ) {
                openWeb(context, "https://www.youtube.com/watch?v=dQw4w9WgXcQ")
            },
        )

        ExpandingIcons(
            expanded = expanded,
            onExpanded = { expanded = !expanded },
            items = items,
            label = stringResource(id = R.string.other_resources),
            otherIcons = {
                LinksHelp()
            },
        )

        return@Card
    }
}

@Composable
private fun LinksHelp(modifier: Modifier = Modifier) {

    var dialogShown by rememberMutableSaveable { mutableStateOf(false) }

    IconButton(
        onClick = { dialogShown = !dialogShown },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Help,
            contentDescription = stringResource(id = R.string.ads_icon_content_description)
        )
    }

    if (dialogShown) {
        AlertDialog(
            onDismissRequest = { dialogShown = false },
            title = {
                Text(text = stringResource(R.string.ads_title))
            },
            text = {
                Text(text = stringResource(R.string.ads_text))
            },
            confirmButton = {
                Button(onClick = { dialogShown = false }) {
                    Text(
                        text = stringResource(id = R.string.ads_ok),
                        textAlign = TextAlign.Center,
                    )
                }
            },
        )
    }
}

private fun openWeb(context: Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

@Composable
private fun BuggyUI(modifier: Modifier = Modifier) {
    var shown by remember { mutableStateOf(false) }

    Button(onClick = { shown = !shown }, modifier) {
        IconTextRow(
            Icons.Default.BugReport,
            text = stringResource(id = R.string.buggy_button)
        )
    }

    if (shown) {
        AlertDialog(
            onDismissRequest = { shown = false },
            title = {
                Text(
                    text = stringResource(id = R.string.buggy_title),
                    modifier = Modifier.padding(4.dp)
                )
            },
            text = {
                Text(stringResource(id = R.string.buggy_text))
            },
            confirmButton = {
                TextButton(onClick = { shown = false }) {
                    Text(
                        stringResource(id = R.string.buggy_ok),
                        textAlign = TextAlign.Center,
                    )
                }
            },
        )
    }
}

@Composable
private fun WhatsNew(modifier: Modifier = Modifier) {

    var shown by remember { mutableStateOf(false) }

    OutlinedButton(onClick = { shown = !shown }, modifier) {
        IconTextRow(
            Icons.Default.Description,
            text = stringResource(id = R.string.whats_new)
        )
    }

    WhatsNewDialog(shown = shown, visibilityChanged = { shown = it })
}

@Composable
private fun ShowPrivacyPolicy(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            PrivacyPolicy.showPolicies(context)
        }, modifier = modifier
    ) {
        IconTextRow(
            Icons.Default.Policy,
            text = stringResource(id = R.string.privacy_policy),
        )
    }
}

@Composable
private fun Licenses(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    OutlinedButton(
        onClick = {
            OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.licenses_title))
            context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        },
        modifier = modifier
    ) {
        IconTextRow(
            Icons.Default.InsertDriveFile,
            text = stringResource(id = R.string.license_notices),
            iconSize = iconSize
        )
    }
}

@Composable
fun Version(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val text = try {
        val version = context.getVersionName()
        val build = context.getVersionCode()

        "$version $build"
    } catch (e: NameNotFoundException) {
        e.printStackTrace()

        stringResource(id = R.string.unknown_version)
    }

    Text(text, modifier)
}
