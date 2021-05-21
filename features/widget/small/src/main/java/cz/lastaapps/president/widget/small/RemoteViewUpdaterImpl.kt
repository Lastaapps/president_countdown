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

package cz.lastaapps.president.widget.small

import cz.lastaapps.president.widget.core.widget.RemoteViewUpdater


/**
 * Manages all the work with remote view for the core
 * */
object RemoteViewUpdaterImpl : RemoteViewUpdater() {

    override val pendingRequest = 48131
    override val pinningRequestCode = 58301
    override val configActivity = WidgetConfigActivityImpl::class

    override val preferredAspectRation: Float get() = 1f / 1

    override val layoutId get() = R.layout.small_widget

    override val values = listOf(null, R.id.dv)

    override val units = listOf(null, R.id.du)

    override val borders: List<Int> =
        listOf(R.id.border_top, R.id.border_bottom, R.id.border_start, R.id.border_end)
    override val rootId: Int get() = android.R.id.background
    override val stateId: Int get() = R.id.state
    override val timeId: Int get() = R.id.time
    override val backgroundId: Int get() = android.R.id.background
    override val yearId: Int? get() = null

}