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
    compileSdk = App.COMPILE_SDK
    buildToolsVersion = App.BUILD_TOOLS

    defaultConfig {
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK

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
        isCoreLibraryDesugaringEnabled = true

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
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
}

dependencies {

    coreLibraryDesugaring(Libs.DESUGARING)

    implementation(Libs.KOTLIN_STANDART_LIB)
    implementation(Libs.KOTLIN_COROUTINES)

    implementation(project(":ui:common"))
    implementation(project(":ui:composeimpl"))
    implementation(project(":ui:colorpicker"))

    implementation(Libs.MATERIAL)

}