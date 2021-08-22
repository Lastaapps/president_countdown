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

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "President Countdown"

include(":app")
include(":app:firebase")
include(":app:navigation")
include(":core:assets")
include(":core:constants")
include(":core:core")
include(":features:about")
include(":features:battery")
include(":features:notifications")
include(":features:privacypolicy")
include(":features:wallpaper")
include(":features:whatsnew")
include(":features:widget:big")
include(":features:widget:core")
include(":features:widget:small")
include(":features:widget:wrapper")
include(":lastaapps:common")
include(":ui:clock")
include(":ui:colorpicker")
include(":ui:common")
include(":ui:composeimpl")
include(":ui:settings")
include(":ui:socials")
