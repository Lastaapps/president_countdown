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

package cz.lastaapps.president.widget.config

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.lastaapps.president.widget.config.database.WidgetDatabase
import cz.lastaapps.president.widget.widget.RemoteViewUpdater

@Entity(tableName = WidgetDatabase.TABLE_WIDGET_STATE)
internal data class WidgetState(
    @ColumnInfo(name = "id") @PrimaryKey val id: Int,
    @ColumnInfo(name = "theme") @WidgetThemeMode val theme: Int,
    @ColumnInfo(name = "light_foreground") val lightForeground: Int,
    @ColumnInfo(name = "dark_foreground") val darkForeground: Int,
    @ColumnInfo(name = "light_background") val lightBackground: Int,
    @ColumnInfo(name = "dark_background") val darkBackground: Int,
    @ColumnInfo(name = "light_differ_year") val lightDifferYear: Boolean,
    @ColumnInfo(name = "dark_differ_year") val darkDifferYear: Boolean,
    @ColumnInfo(name = "light_year_color") val lightYearColor: Int,
    @ColumnInfo(name = "dark_year_color") val darkYearColor: Int,
) {

    companion object {

        fun createDefault(id: Int) =
            WidgetState(
                id = id,
                theme = WidgetThemeMode.SYSTEM,
                lightForeground = Color(0xff000000).toArgb(),
                lightBackground = Color(0xffffffff).toArgb(),
                darkForeground = Color(0xffffffff).toArgb(),
                darkBackground = Color(0xff333333).toArgb(),
                lightDifferYear = true,
                darkDifferYear = true,
                lightYearColor = Color(0xffff0000).toArgb(),
                darkYearColor = Color(0xffff0000).toArgb(),
            )
    }
}

internal fun WidgetState.getForeground(isLight: Boolean): Int =
    if (isLight) lightForeground else darkForeground

internal fun WidgetState.getBackground(isLight: Boolean): Int =
    if (isLight) lightBackground else darkBackground

internal fun WidgetState.isDifferYear(isLight: Boolean): Boolean =
    (isLight && lightDifferYear) || (!isLight && darkDifferYear)

internal fun WidgetState.getYearColor(isLight: Boolean): Int? {
    return if (isDifferYear(isLight)) {
        if (isLight) lightYearColor else darkYearColor
    } else null
}

internal fun WidgetState.getForeground(context: Context): Int =
    getForeground(WidgetThemeMode.isLight(context, theme))

internal fun WidgetState.getBackground(context: Context): Int =
    getBackground(WidgetThemeMode.isLight(context, theme))

internal fun WidgetState.isDifferYear(context: Context): Boolean =
    isDifferYear(WidgetThemeMode.isLight(context, theme))

internal fun WidgetState.getYearColor(context: Context): Int? =
    getYearColor(WidgetThemeMode.isLight(context, theme))


internal fun List<WidgetState>.getById(id: Int): WidgetState? {
    for (state in this) {
        if (state.id == id) return state
    }
    return null
}

internal fun RemoteViewUpdater.updateColors(
    isLight: Boolean,
    views: RemoteViews,
    state: WidgetState
) {
    updateColors(
        views,
        state.getForeground(isLight),
        state.getBackground(isLight),
        state.getYearColor(isLight)
    )
}

internal fun RemoteViewUpdater.updateColors(
    context: Context,
    views: RemoteViews,
    state: WidgetState
) {
    updateColors(
        views,
        state.getForeground(context),
        state.getBackground(context),
        state.getYearColor(context)
    )
}