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

package cz.lastaapps.president.widget.big

import android.content.ComponentName
import android.content.Context
import cz.lastaapps.president.core.president.CurrentState
import cz.lastaapps.president.widget.core.config.WidgetState
import cz.lastaapps.president.widget.core.widget.RemoteViewUpdater
import cz.lastaapps.president.widget.core.widget.SimpleUpdate
import cz.lastaapps.president.widget.core.widget.Widget

/**
 * Implementation of App Widget functionality.
 */
class WidgetImpl : Widget() {

    override val TAG: String
        get() = WidgetImpl::class.simpleName ?: "null"
    override val viewUpdater: RemoteViewUpdater
        get() = RemoteViewUpdaterImpl

    override fun getComponentName(context: Context): ComponentName {
        return ComponentName(context, WidgetImpl::class.java)
    }

    companion object : SimpleUpdate {

        /**
         * Updates all widgets corresponding to the state given
         * */
        override fun updateAllWithState(
            context: Context,
            state: CurrentState,
            themes: List<WidgetState>
        ) = updateAllWithState(
            context, RemoteViewUpdaterImpl,
            state, themes,
            ComponentName(context, WidgetImpl::class.java),
        )

        /**
         * Updates the widgets given corresponding to the state given
         * */
        override fun updateWithState(
            context: Context,
            ids: IntArray,
            state: CurrentState,
            themes: List<WidgetState>
        ) = updateWithState(
            context, RemoteViewUpdaterImpl,
            ids, state, themes,
        )
    }
}