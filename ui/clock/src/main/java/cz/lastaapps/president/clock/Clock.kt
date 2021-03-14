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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.core.president.get

@Composable
fun CreateClock(modifier: Modifier = Modifier) {

    val scope = rememberCoroutineScope()
    val state = remember { CurrentState.getCurrentState(scope) }.collectAsState()

    Clock(state.value, modifier = modifier)
}

@Composable
private fun Clock(state: CurrentState, modifier: Modifier = Modifier) = Box(modifier = modifier) {

    Box(modifier = modifier) {
        if (state.state.isTimeRemainingSupported) {

            val context = LocalContext.current
            val tp = remember(context) { TimePlurals(context) }

            Row {
                DigitLayout(
                    digit = state.sYears,
                    unit = tp.getYears(state.years.toInt()),
                    isRed = true
                )

                for (i in 1 until 5) {
                    DigitLayout(
                        digit = state.sByIndex(i),
                        unit = tp.getByIndex(i, state[i].toInt()),
                    )
                }
            }
        } else {
            Text(text = stringResource(id = state.state.stringId))
        }
    }
}

@Composable
private fun DigitLayout(
    digit: String,
    unit: String,
    modifier: Modifier = Modifier,
    isRed: Boolean = false
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Digit(text = digit, isRed = isRed)

        Unit(text = unit)
    }
}

@Composable
private fun Digit(text: String, modifier: Modifier = Modifier, isRed: Boolean = false) {
    Text(
        text = text,
        style = digitTextStyle,
        color = if (isRed) redDigitColor else Color.Unspecified,
        modifier = modifier
    )
}

@Composable
private fun Unit(text: String) = Text(
    text = text,
    style = unitFonts
)

