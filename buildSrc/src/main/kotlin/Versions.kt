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

import org.gradle.api.JavaVersion

/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object Versions {

    val JAVA = JavaVersion.VERSION_11
    const val JVM_TARGET = "11"

    //Versions
    //Studio
    const val DESUGAR = "1.1.5"

    //JetBrains
    const val KOTLIN = "1.5.21"
    const val KSP = "$KOTLIN-1.0.0-beta07"
    const val COROUTINES = "1.5.1-native-mt"

    //androidx
    const val ACTIVITY = "1.3.1"
    const val APPCOMPAT = "1.3.1"
    const val ANNOTATION = "1.3.0-alpha01"
    const val COLLECTION = "1.2.0-alpha01"
    const val CONSTRAINTLAYOUT = "2.1.0"
    const val CORE = "1.7.0-alpha01"
    const val DATASTORE = "1.0.0"
    const val ROOM = "2.4.0-alpha04"
    const val STARTUP = "1.1.0"
    const val WORK = "2.7.0-alpha05"


    //compose
    const val COMPOSE = "1.1.0-alpha02"
    const val CONSTRAINTLAYOUT_COMPOSE = "1.0.0-beta02"
    const val NAVIGATION = "2.4.0-alpha07"
    const val VIEWMODEL_COMPOSE = "1.0.0-alpha07"

    //google
    const val GOOGLE_MATERIAL = "1.4.0"
    const val OSS_LICENSE = "17.0.0"
    const val PLAY_SERVICES = "1.8.1"

    //firebase
    const val FIREBASE_BOM = "26.4.0"

    //testing
    const val TEST_JUNIT = "4.13.2"
    const val TEST_ARCH = "2.1.0"
    const val TEST_ANDROIDX = "1.4.0"
    const val TEST_ANDROIDX_JUNIT = "1.1.3"
    const val TEST_ESPRESSO_CORE = "3.4.0"
    const val TEST_KOTLIN_COROUTINES = COROUTINES
    const val TEST_ROBOELECTRIC = "4.6.1"
    const val TEST_GOOGLE_TRUTH = "1.1.3"

}
