@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.bilibili.android.application)
    alias(libs.plugins.bilibili.android.compose)
    alias(libs.plugins.bilibili.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}
ksp {
    arg("ModuleName", project.name)
}
android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        applicationId = AndroidConfigConventions.BilibiliAS.APPLICATION_ID
        versionCode = AndroidConfigConventions.BilibiliAS.VERSION_CODE
        versionName = AndroidConfigConventions.BilibiliAS.VERSION_NAME

        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            // excludes += "META-INF/DEPENDENCIES"
            // excludes += "META-INF/LICENSE"
            // excludes += "META-INF/LICENSE.txt"
            // excludes += "META-INF/license.txt"
            // excludes += "META-INF/NOTICE"
            // excludes += "META-INF/NOTICE.txt"
            // excludes += "META-INF/notice.txt"
            // excludes += "META-INF/ASL2.0"
            // excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:design-system"))
    implementation(project(":core:ui"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))

    implementation(project(":feature:authentication"))
    implementation(project(":feature:home"))
    implementation(project(":feature:tool"))
    implementation(project(":feature:user"))
    implementation(project(":feature:setting"))
    implementation(project(":feature:player"))

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.compose.material3)

    implementation(libs.material)

    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.androidx.activity.compose)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.compose.settings.ui.m3)

    /**
     * ffmpeg
     */
    implementation(libs.ffmpeg.kit.full)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.coil.kt.gif)
}
