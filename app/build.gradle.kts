@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.application.compose)
    alias(libs.plugins.bilibilias.android.application.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
    id("jacoco")
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

ksp {
    arg("ModuleName", project.name)
}
android {
    namespace = "com.imcys.bilibilias"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        minSdk = 21
        // noinspecton ExpiredTargetSdkVersion
        targetSdk = 34
        versionCode = 203
        versionName = "2.0.4-开阳-Alpha"

        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        flavorDimensions += project.name
    }

    buildTypes {
        debug {
            // 混淆
            isMinifyEnabled = true
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
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = false
        checkReleaseBuilds = false
    }

    buildFeatures {
        compose = true
    }

    dataBinding {
        enable = true
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
    implementation(project(":common"))
    implementation(project(":model_ffmpeg"))
    implementation(project(":tool_log_export"))

    ksp(libs.deeprecopy.compiler)

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.hilt.android)
    implementation(libs.work.runtime.ktx)
    ksp(libs.hilt.compiler)

    ksp(libs.kcomponent.compiler)
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
}
dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}