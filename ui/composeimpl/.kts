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

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = App.compile_sdk_version
    buildToolsVersion = App.build_tools_version

    defaultConfig {
        minSdk = App.min_sdk_version
        targetSdk = App.target_sdk_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        multiDexEnabled = true
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Versions.JAVA
        targetCompatibility = Versions.JAVA
    }
    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
    }
    buildFeatures {
        compose = true
        buildConfig = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {

    api "androidx.compose.animation:animation:$compose_version"
    api "androidx.compose.compiler:compiler:$compose_version"
    api "androidx.compose.material:material:$compose_version"
    api "androidx.compose.material:material-icons-core:$compose_version"
    api "androidx.compose.material:material-icons-extended:$compose_version"
    api "androidx.compose.foundation:foundation:$compose_version"
    api "androidx.compose.runtime:runtime-livedata:$compose_version"
    api "androidx.compose.ui:ui:$compose_version"
    api "androidx.compose.ui:ui-tooling:$compose_version"

    api "androidx.activity:activity-compose:$activity_version"
    api "androidx.constraintlayout:constraintlayout-compose:$constraintlayout_compose_version"
    api "androidx.lifecycle:lifecycle-viewmodel-compose:$viewmodel_version"
    api "androidx.navigation:navigation-compose:$navigation_version"

    //layout inspector support
    api(Libs.KOTLIN_REFLECT)

    androidTestApi(Tests.COMPOSE)
}