plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.compose)
    alias(libs.plugins.bilibilias.hilt)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

android {
    namespace = "com.imcys.bilibilias"
    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        versionCode = 207
        versionName = "2.0.46"
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        flavorDimensions += project.name
    }

    buildTypes {
        debug {
            resValue("string", "app_name", "@string/app_name_debug")
            resValue("string", "app_channel", "@string/app_channel_debug")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            resValue("string", "app_name", "@string/app_name_release")
            resValue("string", "app_channel", "@string/app_channel_release")
            signingConfig = signingConfigs["BilibiliAsSigningConfig"]
        }
    }
    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    splits {
        abi {
            isEnable = true
            reset()
            isUniversalApk = false
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":tool_log_export"))
    implementation(libs.androidx.activity)

    ksp(libs.deeprecopy.compiler)

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.work.runtime.ktx)
    ksp(libs.hilt.compiler)

    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

//    implementation(libs.ffmpeg.kit.full)
}
