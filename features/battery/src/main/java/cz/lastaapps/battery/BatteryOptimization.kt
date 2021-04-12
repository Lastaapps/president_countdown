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

package cz.lastaapps.battery

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

internal fun showAppInfo(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:${context.packageName}")
    context.startActivity(intent)
}

internal fun showBatterySettings(context: Context) {
    context.startActivity(Intent(Settings.ACTION_SETTINGS))
}

internal fun shouldShowBatteryOptimizationDialog(context: Context): Boolean {

    val mgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        mgr.isBackgroundRestricted
    else
        false

    //TODO test on pre Pie devices
    /*val pm: PowerManager? = context.getSystemService(POWER_SERVICE) as PowerManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (pm != null && !pm.isIgnoringBatteryOptimizations(context.packageName)) {
            return true
        }
    }
    return false*/
}

