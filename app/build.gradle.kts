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
    id("com.android.application")

    id("kotlin-android")

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {

    if (App.use_legacy) {
        compileSdk = App.compile_sdk_version
        buildToolsVersion = App.build_tools_version
    } else {
        compileSdk = App.legacy_compile_sdk_version
        buildToolsVersion = App.legacy_build_tools_version
    }

    defaultConfig {
        applicationId = App.app_id
        minSdk = App.min_sdk_version
        versionCode = App.app_version_code
        versionName = App.app_version_name

        targetSdk = if (App.use_legacy) App.target_sdk_version else App.legacy_target_sdk_version

        resourceConfigurations.addAll(setOf("en", "cs"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        multiDexEnabled = true
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"

            extra.set("alwaysUpdateBuildId", false)

            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
}

dependencies {

    coreLibraryDesugaring(Libs.DESUGARING)

    //implementation(fileTree(dir: "libs", include: ["*.jar"]))
    implementation(Libs.KOTLIN_STANDART_LIB)
    implementation(Libs.KOTLIN_COROUTINES)

    implementation(project(":app:firebase"))
    implementation(project(":app:navigation"))
    implementation(project(":core:assets"))
    implementation(project(":core:core"))
    implementation(project(":features:about"))
    implementation(project(":features:notifications"))
    implementation(project(":features:privacypolicy"))
    implementation(project(":features:wallpaper"))
    implementation(project(":features:whatsnew"))
    implementation(project(":features:widget:wrapper"))
    implementation(project(":lastaapps:common"))
    implementation(project(":ui:clock"))
    implementation(project(":ui:common"))
    implementation(project(":ui:composeimpl"))
    implementation(project(":ui:socials"))

    implementation(Libs.APPCOMPAT)
    implementation(Libs.ANNOTATION)
    implementation(Libs.COLLECTION)
    implementation(Libs.CORE)
    implementation(Libs.DATASTORE)
    implementation(Libs.STARTUP)
    implementation(Libs.WORK)

    implementation(Libs.OSS_LICENSE)
    implementation(Libs.PLAY_SERVICES)

    implementation(Libs.FIREBASE_BOM)
    implementation(Libs.FIREBASE_ANALYTICS)
    implementation(Libs.FIREBASE_CONFIG)
    implementation(Libs.FIREBASE_CRASHLYTICS)
    implementation(Libs.FIREBASE_MESSAGEING)

    testImplementation(Tests.JUNIT)
    androidTestImplementation(Tests.ANDROIDX_JUNIT)
    androidTestImplementation(Tests.ESPRESSO)
}