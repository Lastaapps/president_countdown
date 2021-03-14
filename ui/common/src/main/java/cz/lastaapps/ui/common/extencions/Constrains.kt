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

import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference

/*
* Functions to add padding in constrain layout when aligning center[Vertically|Horizontally]
* */

fun ConstrainScope.centerToWithPadding(other: ConstrainedLayoutReference, padding: Dp) {
    centerVerticallyToWithPadding(other = other, padding = padding)
    centerHorizontallyToWithPadding(other = other, padding = padding)
}

fun ConstrainScope.centerVerticallyToWithPadding(other: ConstrainedLayoutReference, padding: Dp) {
    top.linkTo(other.top, padding)
    bottom.linkTo(other.bottom, padding)
}

fun ConstrainScope.centerHorizontallyToWithPadding(other: ConstrainedLayoutReference, padding: Dp) {
    start.linkTo(other.start, padding)
    end.linkTo(other.end, padding)
}