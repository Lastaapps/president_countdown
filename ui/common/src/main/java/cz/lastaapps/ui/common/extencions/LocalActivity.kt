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

package cz.lastaapps.ui.common.extencions

import android.app.Activity
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Access to the activity in which is the compose tree placed
 * */
object LocalActivity {

    const val indexMax = 64

    /**
     * @return Activity of the given type in which is the compose placed
     * */
    @Composable
    inline fun <reified T : Activity> get(): T? {
        var context = LocalContext.current
        var index = 0

        return remember(context) {
            try {
                while (context !is T) {
                    if (index++ > indexMax)
                        throw IllegalStateException("To many cycles, max is $indexMax")

                    context = (context as ContextThemeWrapper).baseContext
                }

                context
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } as T?
        }
    }

    @Composable
    fun getCompat(): AppCompatActivity? = get()

}