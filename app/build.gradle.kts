@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibiliAs.android.application.compose)
    alias(libs.plugins.bilibiliAs.android.application)
    alias(libs.plugins.bilibiliAs.android.hilt)
    id("jacoco")
}

android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        applicationId = AndroidConfigConventions.BilibiliAS.APPLICATION_ID
        versionCode = AndroidConfigConventions.BilibiliAS.VERSION_CODE
        versionName = AndroidConfigConventions.BilibiliAS.VERSION_NAME

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "com.bilias.core.testing.AsTestRunner"
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            resValue("string", "app_name", "@string/app_name_debug")
            resValue("string", "app_channel", "@string/app_channel_debug")
            buildConfigField("boolean", "LOG_DEBUG", "true")
        }
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = ".release"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            resValue("string", "app_name", "@string/app_name_debug")
            resValue("string", "app_channel", "@string/app_channel_debug")
            buildConfigField("boolean", "LOG_DEBUG", "false")
        }
    }

    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "META-INF/*******"
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(projects.core.crash)

    implementation(projects.feature.authentication)
    implementation(projects.feature.home)
    implementation(projects.feature.tool)
    implementation(projects.feature.user)
    implementation(projects.feature.setting)
    implementation(projects.feature.player)
    implementation(projects.feature.userspace)
    implementation(projects.feature.merge)
    implementation(projects.feature.download)

    implementation(project(":XXPermissions:library"))

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.material3)

    implementation(libs.material)

    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.androidx.activity.compose)

    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.compose.settings.ui.m3)

    /**
     * ffmpeg
     */
    implementation(libs.ffmpeg.kit.full)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.gif)
    debugImplementation(libs.leakcanary.android)
}
