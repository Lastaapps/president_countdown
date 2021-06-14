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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


/**
 * Loads and stores images for a portrait and landscape wallpaper background
 * */
object ImageRepo {

    private val TAG get() = ImageRepo::class.simpleName

    private const val folderName = "wallpapers"
    private const val portName = "portrait.png"
    private const val landName = "landscape.png"

    private var portraitBitmaps: MutableStateFlow<Bitmap?>? = null
    private var landscapeBitmaps: MutableStateFlow<Bitmap?>? = null

    /**
     * @return list of flows to be observed for a change
     * */
    fun listAllVariables(context: Context): List<StateFlow<Any?>> = listOf(
        getBitmaps(context, true), getBitmaps(context, false),
    )

    /**
     * loads bitmap flow for orientation given
     * */
    fun getBitmaps(context: Context, isPortrait: Boolean): StateFlow<Bitmap?> = synchronized(this) {

        Log.i(TAG, "Getting bitmap, portrait: $isPortrait")

        return if (isPortrait) {
            if (portraitBitmaps == null)
                portraitBitmaps = MutableStateFlow(loadFile(context, true))
            portraitBitmaps!!
        } else {
            if (landscapeBitmaps == null)
                landscapeBitmaps = MutableStateFlow(loadFile(context, true))
            landscapeBitmaps!!
        }
    }

    fun setBitmaps(context: Context, bitmap: Bitmap?, isPortrait: Boolean) = synchronized(this) {

        Log.i(TAG, "Saving bitmap, portrait: $isPortrait")

        if (isPortrait) {
            portraitBitmaps?.tryEmit(bitmap)
        } else {
            landscapeBitmaps?.tryEmit(bitmap)
        }

        if (bitmap != null)
            saveFile(context, bitmap, isPortrait)
        else
            delete(context, isPortrait)
    }

    private fun loadFile(context: Context, isPortrait: Boolean): Bitmap? {
        return try {

            val file = getFile(context, isPortrait)
            BitmapFactory.decodeStream(FileInputStream(file))

        } catch (e: Exception) {
            e.printStackTrace()

            null
        }
    }

    private fun saveFile(context: Context, bitmap: Bitmap, isPortrait: Boolean) {
        try {

            val file = getFile(context, isPortrait)

            val fos: FileOutputStream = FileOutputStream(file)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun delete(context: Context, isPortrait: Boolean) {
        try {
            val file = getFile(context, isPortrait)
            file.delete()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Copies wallpaper from one mode to the another
     * @param isPortraitSource if portrait mode is the source, otherwise landscape is
     * */
    fun copyToOther(context: Context, isPortraitSource: Boolean) {

        Log.i(TAG, "Copying data, portrait: $isPortraitSource")

        setBitmaps(context, getBitmaps(context, isPortraitSource).value, !isPortraitSource)
    }

    private fun getFile(context: Context, isPortrait: Boolean): File {
        val folder = context.getDir(folderName, Context.MODE_PRIVATE)
        folder.mkdirs()
        folder.mkdir()

        val fileName = if (isPortrait) portName else landName
        return File(folder, fileName).also {
            if (!it.exists())
                it.createNewFile()
        }
    }
}