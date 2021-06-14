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

import android.annotation.SuppressLint
import android.util.Log
import android.view.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

private const val TAG = "ComposeFunctions"

/**
 * The same as libraries #viewModel(), but this one accepts KClass instead of kotlin Class
 * */
@Composable
fun <T : ViewModel> viewModelKt(
    modelClass: KClass<T>,
    key: String? = null,
    factory: ViewModelProvider.Factory? = null
): T = androidx.lifecycle.viewmodel.compose.viewModel(modelClass.java, key, factory)

/**
 * changes status bar color to selected color
 * */
@SuppressLint("ComposableNaming")
@Composable
fun updateStatusBar(
    statusBarColor: Color = MaterialTheme.colors.primaryVariant
) {
    val activity = LocalActivity.getCompat()

    remember(activity, statusBarColor) {
        if (activity != null) {
            Log.i(TAG, "Updating status bar color")

            val window: Window = activity.window

            val controller = WindowInsetsControllerCompat(window, window.decorView)

            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH

            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            window.statusBarColor = statusBarColor.toArgb()
        }
        null
    }
}

