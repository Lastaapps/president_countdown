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

package cz.lastaapps.president.app.ui.uimode

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.lastaapps.president.app.R

/**
 * Shows a icon representing the current UI mode
 * */
@Composable
fun UIModeSwitch(
    @UIModeState uiMode: Int,
    uiModeChanged: (theme: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    //selects the appropriate icon
    val icon = when (uiMode) {
        UIModeState.SYSTEM -> Icons.Default.InvertColors
        UIModeState.DAY -> Icons.Default.WbSunny
        UIModeState.NIGHT -> Icons.Default.Bedtime
        else -> throw IllegalArgumentException("Invalid ${UIModeState::class.simpleName} value: $uiMode")
    }

    IconButton(
        onClick = { uiModeChanged(UIModeState.next(uiMode)) },
        modifier = modifier
    ) {
        Icon(
            icon,
            contentDescription = stringResource(id = R.string.content_description_theme)
        )
    }
}