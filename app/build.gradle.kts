plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.application.compose)
    alias(libs.plugins.bilibilias.android.application.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

android {
    namespace = "com.imcys.bilibilias"
    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        versionCode = 204
        versionName = "2.0.41"
        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        flavorDimensions += project.name
    }

    buildTypes {
        debug {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            resValue("string", "app_name", "@string/app_name_debug")
            resValue("string", "app_channel", "@string/app_channel_debug")
        }

        release {
            // 混淆
            isMinifyEnabled = true
            // 移除无用的resource文件
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            resValue("string", "app_name", "@string/app_name_release")
            resValue("string", "app_channel", "@string/app_channel_release")
            baselineProfile.automaticGenerationDuringBuild = true
        }
    }

    buildFeatures {
        compose = true
        dataBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(projects.common)
    implementation(projects.toolLogExport)

    implementation(projects.core.common)
    implementation(projects.core.crash)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.download)

    implementation(projects.feature.home)
    implementation(projects.feature.tool)
    implementation(projects.feature.download)
    implementation(projects.feature.user)

    implementation(libs.androidx.activity.compose)
    implementation("androidx.compose.ui:ui-viewbinding")
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
//    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
//    implementation(libs.androidx.window.core)
    implementation(libs.coil.kt)

    ksp(libs.deeprecopy.compiler)
    ksp(libs.hilt.compiler)

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.work.runtime.ktx)

    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation("com.github.alexzhirkevich:custom-qr-generator:2.0.0-alpha01")
}
baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}