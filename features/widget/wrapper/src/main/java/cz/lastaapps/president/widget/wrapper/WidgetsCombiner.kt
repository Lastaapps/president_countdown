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

package cz.lastaapps.president.widget.wrapper

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.big.BigConfigActivity
import cz.lastaapps.president.widget.big.BigViewUpdater
import cz.lastaapps.president.widget.big.BigWidget
import cz.lastaapps.president.widget.core.config.WidgetState
import cz.lastaapps.president.widget.small.SmallConfigActivity
import cz.lastaapps.president.widget.small.SmallViewUpdater
import cz.lastaapps.president.widget.small.SmallWidget

internal object WidgetsCombiner {

    val modules = listOf(
        ModuleSpec(
            BigWidget::class, BigWidget, BigConfigActivity::class,
            cz.lastaapps.president.widget.big.R.string.widget_big_name,
            BigViewUpdater,
        ),
        ModuleSpec(
            SmallWidget::class, SmallWidget, SmallConfigActivity::class,
            cz.lastaapps.president.widget.small.R.string.widget_small_name,
            SmallViewUpdater,
        ),
    )
    val providers = modules.map { it.widget }
    val companions = modules.map { it.companion }

    fun updateAllWithState(
        context: Context,
        state: CurrentState,
        themes: List<WidgetState>,
    ) {
        companions.forEach {
            it.updateAllWithState(context, state, themes)
        }
    }

    fun checkWidgetsPresented(context: Context): Boolean {
        val mgr = AppWidgetManager.getInstance(context)
        providers.map {
            mgr.getAppWidgetIds(ComponentName(context, it.java))
        }.forEach {
            if (it.isNotEmpty())
                return true
        }

        return false
    }
}