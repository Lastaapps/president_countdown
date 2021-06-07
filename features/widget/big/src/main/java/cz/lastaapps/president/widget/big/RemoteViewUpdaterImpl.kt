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

import cz.lastaapps.president.widget.core.widget.RemoteViewUpdater


/**
 * Manages all the work with remote view for the core
 * */
object RemoteViewUpdaterImpl : RemoteViewUpdater() {

    override val pendingRequest = 48130
    override val pinningRequestCode = 58300
    override val configActivity = WidgetConfigActivityImpl::class

    override val preferredAspectRation: Float get() = 4f / 1

    override val layoutId get() = R.layout.big_widget

    override val values = listOf(R.id.yv, R.id.dv, R.id.hv, R.id.mv, R.id.sv)

    override val units = listOf(R.id.yu, R.id.du, R.id.hu, R.id.mu, R.id.su)

    override val borders: List<Int> =
        listOf(R.id.border_top, R.id.border_bottom, R.id.border_start, R.id.border_end)
    override val rootId: Int get() = R.id.background //TODO change later
    override val stateId: Int get() = R.id.state
    override val timeId: Int get() = R.id.time
    override val backgroundId: Int get() = R.id.background //TODO change later
    override val yearId: Int get() = R.id.yv

}