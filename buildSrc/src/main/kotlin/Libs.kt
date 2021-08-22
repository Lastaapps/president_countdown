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

object Libs {

    const val DESUGARING = "com.android.tools:desugar_jdk_libs:${Versions.DESUGAR}"

    const val KOTLIN_STANDART_LIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
    const val KOTLIN_COROUTINES =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"

    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANNOTATION = "androidx.annotation:annotation:${Versions.ANNOTATION}"
    const val COLLECTION = "androidx.collection:collection-ktx:${Versions.COLLECTION}"
    const val CONSTRAINTLAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINTLAYOUT}"
    const val CORE = "androidx.core:core-ktx:${Versions.CORE}"
    const val DATASTORE = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"
    const val LIVEDATA = "androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE}"
    const val STARTUP = "androidx.startup:startup-runtime:${Versions.STARTUP}"
    const val WORK = "androidx.work:work-runtime-ktx:${Versions.WORK}"

    const val ROOM = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"

    const val MATERIAL = "com.google.android.material:material:${Versions.GOOGLE_MATERIAL}"
    const val OSS_LICENSE =
        "com.google.android.gms:play-services-oss-licenses:${Versions.OSS_LICENSE}"
    const val PLAY_SERVICES = "com.google.android.play:core-ktx:${Versions.PLAY_SERVICES}"

    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
    const val FIREBASE_CONFIG = "com.google.firebase:firebase-config-ktx"
    const val FIREBASE_MESSAGEING = "com.google.firebase:firebase-messaging-ktx"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"


    const val COMPOSE_ANIMATION = "androidx.compose.animation:animation:${Versions.COMPOSE}"
    const val COMPOSE_COMPILER = "androidx.compose.compiler:compiler:${Versions.COMPOSE}"
    const val COMPOSE_MATERIAL = "androidx.compose.material:material:${Versions.COMPOSE}"
    const val COMPOSE_ICONS_CORE =
        "androidx.compose.material:material-icons-core:${Versions.COMPOSE}"
    const val COMPOSE_ICONS_EXTENDED =
        "androidx.compose.material:material-icons-extended:${Versions.COMPOSE}"
    const val COMPOSE_FOUNDATION = "androidx.compose.foundation:foundation:${Versions.COMPOSE}"
    const val COMPOSE_UI = "androidx.compose.ui:ui:${Versions.COMPOSE}"
    const val COMPOSE_TOOLING = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"

    const val COMPOSE_ACTIVITY = "androidx.activity:activity-compose:${Versions.ACTIVITY}"
    const val COMPOSE_CONSTRAINTLAYOUT =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.CONSTRAINTLAYOUT_COMPOSE}"
    const val COMPOSE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.VIEWMODEL_COMPOSE}"
    const val COMPOSE_NAVIGATION = "androidx.navigation:navigation-compose:${Versions.NAVIGATION}"

}
