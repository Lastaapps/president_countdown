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

package cz.lastaapps.president.app.ui.rate

import androidx.compose.runtime.*
import cz.lastaapps.common.PlayStoreReview
import cz.lastaapps.ui.common.extencions.LocalActivity

@Composable
fun RatePopup() {

    val activity by LocalActivity.rememberUpdatedState()

    val repo = remember(activity) { RateRepo(activity) }
    val show by repo.state.collectAsState()

    LaunchedEffect(repo) {
        //Toast.makeText(activity, "Added", Toast.LENGTH_LONG).show()
        repo.appOpened()
    }

    if (show) {
        LaunchedEffect(activity) {
            //Toast.makeText(activity, "Shown", Toast.LENGTH_LONG).show()
            PlayStoreReview.askForAppReview(activity)
            repo.shown()
        }
    }
}
