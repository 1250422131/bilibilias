plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.compose)
    alias(libs.plugins.bilibilias.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.imcys.bilibilias"
    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        versionCode = 210
        versionName = "2.1.5-天权-Beta"
        ndk {
            abiFilters += listOf("arm64-v8a","armeabi-v7a", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        flavorDimensions += project.name
    }

    buildTypes {
        debug {
            resValue("string", "app_name", "BILIBILIAS_DEBUG")
            resValue("string", "app_channel", "Official Channel Debug")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            resValue("string", "app_name", "BILIBILIAS")
            resValue("string", "app_channel", "Official Channel")
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

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}
