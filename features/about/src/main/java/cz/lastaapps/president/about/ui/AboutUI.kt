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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.play.core.review.ReviewManagerFactory
import cz.lastaapps.common.Communication
import cz.lastaapps.president.about.R
import cz.lastaapps.president.clock.ClockLayout
import cz.lastaapps.president.constants.playStoreLink
import cz.lastaapps.president.core.functionality.getLocale
import cz.lastaapps.president.core.president.President
import cz.lastaapps.president.privacypolicy.PrivacyPolicy
import cz.lastaapps.president.whatsnew.ui.WhatsNewDialog
import cz.lastaapps.ui.common.components.IconTextRow
import cz.lastaapps.ui.common.components.SocialButton
import cz.lastaapps.ui.common.components.socialIconContent
import cz.lastaapps.ui.common.components.socialImageContent
import cz.lastaapps.ui.common.extencions.iconSize
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
            BuggyUI()

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
    Button(onClick = { rateAction(context) }, modifier = modifier) {
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
    Button(onClick = { shareAction(context) }, modifier = modifier) {
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = stringResource(id = R.string.other_resources))

            Row {

                val context = LocalContext.current

                SocialButton(
                    onClick = { openWeb(context, President.getWikiLink(context.getLocale())) },
                    socialImageContent(
                        id = R.drawable.wikipedia,
                        contentId = R.string.content_description_wikipedia
                    )
                )

                SocialButton(
                    onClick = { openWeb(context, "https://www.hrad.cz") },
                    socialImageContent(
                        id = R.drawable.hrad_cz,
                        contentId = R.string.content_description_hrad
                    )
                )

                SocialButton(
                    onClick = {
                        Communication.openFacebookPage(
                            context,
                            "https://www.facebook.com/KancelarPrezidentaRepubliky/"
                        )
                    },
                    socialImageContent(
                        id = R.drawable.je_milos_zeman_stale_nazivu,
                        contentId = R.string.content_description_jmzsn
                    )
                )

                SocialButton(
                    onClick = {
                        openWeb(
                            context,
                            "https://play.google.com/store/apps/details?id=cz.JMZDP.jemilozemandobrmprezidentem"
                        )
                    },
                    socialImageContent(
                        id = R.drawable.je_milos_zeman_dobrym_prezidentem,
                        contentId = R.string.content_description_jmzdp
                    )
                )

                SocialButton(
                    onClick = {
                        openWeb(
                            context,
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                        )
                    },
                    socialIconContent(
                        Icons.Default.AccessibilityNew,
                        contentId = R.string.content_description_surprice
                    )
                )

            }
        }
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
                    Text(stringResource(id = R.string.buggy_ok))
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

@SuppressLint("ObsoleteSdkInt")
private fun rateAction(context: Context) {
    val manager = ReviewManagerFactory.create(context)

    //redirects to the play store, required under LOLLIPOP 5.0 and when Google play
    //API fails
    val oldRequest = {
        val url = playStoreLink
        val uri = Uri.parse(url)
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    //version check
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        oldRequest()
        return
    }

    //Google play in app review
    val request = manager.requestReviewFlow()
    request.addOnCompleteListener { response ->
        if (response.isSuccessful) {

            val reviewInfo = response.result
            val flow = manager.launchReviewFlow(context as Activity, reviewInfo)
            flow.addOnCompleteListener {
                Toast.makeText(context, R.string.thank_review, Toast.LENGTH_LONG).show()
            }
        } else {
            oldRequest()
        }
    }
}

private fun shareAction(context: Context) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_message) + " $playStoreLink"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Composable
fun Version(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val text = try {
        val pInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        val version = pInfo.versionName
        val build = if (Build.VERSION.SDK_INT >= 28) pInfo.longVersionCode else pInfo.versionCode

        "$version $build"
    } catch (e: NameNotFoundException) {
        e.printStackTrace()

        stringResource(id = R.string.unknown_version)
    }

    Text(text, modifier)
}
