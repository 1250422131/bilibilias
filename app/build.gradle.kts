@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.application.compose)
    alias(libs.plugins.bilibilias.android.application.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
    id("jacoco")
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        versionCode = 203
        versionName = "2.0.4-开阳-Alpha"

        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
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

    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = false
        checkReleaseBuilds = false
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
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(projects.core.crash)
    implementation(projects.core.domain)

    implementation(project(":common"))
    implementation(project(":model_ffmpeg"))
    implementation(project(":tool_log_export"))
    implementation(projects.feature.home)
    implementation(projects.feature.tool)
    implementation(projects.feature.download)
    implementation(projects.feature.user)

    ksp(libs.deeprecopy.compiler)

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.hilt.android)
    implementation(libs.work.runtime.ktx)
    ksp(libs.hilt.compiler)

    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.compose.material3)

    implementation("androidx.compose.ui:ui-viewbinding:1.6.5")
    implementation("com.github.getActivity:Toaster:12.6")

    implementation(libs.compose.destinations)
    ksp(libs.compose.destinations.ksp)
    implementation(libs.compose.destinations.animations)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation("com.github.alexzhirkevich:custom-qr-generator:2.0.0-alpha01")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation(libs.coil.kt)

    implementation("com.holix.android:bottomsheetdialog-compose:1.4.1")
    implementation("io.github.androidpoet:dropdown:1.1.2")
}
baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
}
dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}