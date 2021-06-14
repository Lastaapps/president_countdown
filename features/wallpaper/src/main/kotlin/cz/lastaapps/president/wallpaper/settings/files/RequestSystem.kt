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

package cz.lastaapps.president.wallpaper.settings.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat


private const val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
private const val STORAGE_PERMISSION_REQUEST_CODE = 34527

/**
 * @return if a system wallpaper can be loaded without asking for a permission
 * */
internal fun hasStoragePermissionForSystem(context: Context): Boolean {

    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            context.checkSelfPermission(storagePermission) == PackageManager.PERMISSION_GRANTED
        }
        else -> {
            true
        }
    }
}

/**
 * @return if an image from the storage can be loaded without asking for a permission
 * */
internal fun hasStoragePermissionForFiles(context: Context): Boolean {

    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            true
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            context.checkSelfPermission(storagePermission) == PackageManager.PERMISSION_GRANTED
        }
        else -> {
            true
        }
    }
}

/**
 * Shows system dialog to request the READ_STORAGE permission
 * */
internal fun requestStoragePermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(storagePermission),
        STORAGE_PERMISSION_REQUEST_CODE
    )
}

/**
 * @return if a system grant dialog can be shown
 * */
internal fun canAskForStoragePermission(activity: Activity): Boolean {
    return !ActivityCompat.shouldShowRequestPermissionRationale(activity, storagePermission)
}

/**
 * Opens app info screen, where user can grant the permission himself
 * */
internal fun openPermissionsSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", context.packageName, null)
    intent.data = uri
    context.startActivity(intent)
}
