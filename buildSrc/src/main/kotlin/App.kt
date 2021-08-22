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

    //TODO rename later
    const val app_id = "cz.lastaapps.president.app"
    const val app_version_code = 1010000 // 1x major . 2x minor . 2x path . 2x build diff
    const val app_version_name = "1.1.0"
    const val app_is_alpha = false
    const val app_is_beta = false

    const val use_legacy = false
    const val min_sdk_version = 21

    //latest version, may be preview
    const val compile_sdk_version = 31
    const val build_tools_version = "31.0.0"
    const val target_sdk_version = 31
//    const val compile_sdk_version = "android-S"
//    const val build_tools_version = "31.0.0 rc5"
//    const val target_sdk_version = "S"

    //legacy version, last released android version
    const val legacy_compile_sdk_version = 31
    const val legacy_build_tools_version = "31.0.0"
    const val legacy_target_sdk_version = 31

}