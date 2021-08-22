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

    id("com.google.devtools.ksp")
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

        //room database schemas
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_COMPILER
    }
}

dependencies {

    coreLibraryDesugaring(Libs.DESUGARING)

    implementation(Libs.KOTLIN_STANDART_LIB)
    implementation(Libs.KOTLIN_COROUTINES)

    implementation(project(":core:assets"))
    implementation(project(":core:core"))
    implementation(project(":features:battery"))
    implementation(project(":features:widget:core"))
    implementation(project(":ui:common"))
    implementation(project(":ui:composeimpl"))
    implementation(project(":ui:settings"))

    implementation(Libs.APPCOMPAT)
    implementation(Libs.CORE)

    implementation(Libs.MATERIAL)

}