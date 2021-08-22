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

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

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

object App {

    val buildDate = ZonedDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneId.of("UTC"))
        .toInstant().epochSecond

    const val APP_ID = "cz.lastaapps.president.app"
    const val VERSION_CODE = 1010000 // 1x major . 2x minor . 2x path . 2x build diff
    const val VERSION_NAME = "1.1.0"
    const val IS_ALPHA = false
    const val IS_BETA = false

    const val USE_LEGACY = false
    const val MIN_SDK = 21

    //latest version, may be preview
    const val COMPILE_SDK = 31
    const val BUILD_TOOLS = "31.0.0"
    const val TARGET_SDK = 31
//    const val COMPILE_SDK = "android-S"
//    const val BUILD_TOOLS = "31.0.0 rc5"
//    const val TARGET_SDK = "S"

    //legacy version, last released android version
    const val LEGACY_COMPILE_SDK = 31
    const val LEGACY_BUILD_TOOLS = "31.0.0"
    const val LEGACY_TARGET_SDK = 31

}