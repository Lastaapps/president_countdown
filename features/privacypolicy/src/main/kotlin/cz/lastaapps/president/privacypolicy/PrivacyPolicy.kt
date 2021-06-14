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

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable

/**
 * The entry point for the module
 * */
object PrivacyPolicy {

    @Composable
    fun Dialog(
        shown: Boolean,
        stateChanged: (Boolean) -> Unit,
    ) {
        PolicyDialog(shown, stateChanged)
    }

    fun shouldAgree(context: Context): Boolean {
        return PolicyManager(context).shouldAgree()
    }

    fun showPolicies(context: Context) {
        context.startActivity(Intent(context, ShowPolicesActivity::class.java))
    }
}