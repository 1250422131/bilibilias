@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.application.compose)
    alias(libs.plugins.bilibilias.android.application.jacoco)
    alias(libs.plugins.bilibilias.android.hilt)
    id("jacoco")
    alias(libs.plugins.baselineprofile)
//    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.kapt)
}

//ksp {
//    arg("ModuleName", project.name)
//}
android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        versionCode = 203
        versionName = "2.0.4-开阳-Alpha"

        buildConfigField("String", "APP_CENTER_SECRET", """ + appCenterSecret + """)

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
        dataBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
//
//    dependenciesInfo {
//        includeInApk = true
//        includeInBundle = true
//    }
}
kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":common"))
    implementation(project(":tool_log_export"))

    ksp(libs.deeprecopy.compiler)

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.work.runtime.ktx)

    ksp(libs.hilt.compiler)

    ksp(libs.kcomponent.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
