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

package cz.lastaapps.president.clock

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import cz.lastaapps.president.core.functionality.getLocale
import cz.lastaapps.president.core.president.President

@Composable
fun WebMessages(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RemainsMessage()
        ThankYou()
    }
}

@Composable
private fun RemainsMessage(modifier: Modifier = Modifier) {

    val text = with(AnnotatedString.Builder()) {

        append(stringResource(id = R.string.message_remains))
        append(' ')

        pushStyle(SpanStyle(Color.Red))
        append(stringResource(id = cz.lastaapps.president.assets.R.string.president_nameGenitiv))
        pop()

        append('.')

        toAnnotatedString()
    }

    val context = LocalContext.current
    Box(
        modifier = modifier.clickable {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    President.getWikiLink(context.getLocale()).toUri()
                )
            )
        }
    ) {
        Text(
            text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
private fun ThankYou(modifier: Modifier = Modifier) {
    val text = with(AnnotatedString.Builder()) {

        pushStyle(SpanStyle(Color(0xFF2979FF)))
        append(stringResource(id = R.string.message_thank_you))
        pop()

        toAnnotatedString()
    }

    Text(
        text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.caption,
        modifier = Modifier
            .padding(4.dp)
            .then(modifier),
    )
}
