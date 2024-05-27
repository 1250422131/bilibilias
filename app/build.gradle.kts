import com.imcys.bilibilias.AsBuildType

plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.application.compose)
    alias(libs.plugins.bilibilias.android.application.jacoco)
    alias(libs.plugins.bilibilias.android.application.flavors)
    alias(libs.plugins.bilibilias.multiplatform.decompose)
//    alias(libs.plugins.bilibilias.android.application.bugly)
    alias(libs.plugins.bilibilias.android.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.buildconfig)
//    id("com.google.gms.google-services")
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
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = AsBuildType.DEBUG.applicationIdSuffix
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
            applicationIdSuffix = AsBuildType.RELEASE.applicationIdSuffix
            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
            baselineProfile.automaticGenerationDuringBuild = true

            resValue("string", "app_name", "@string/app_name_release")
            resValue("string", "app_channel", "@string/app_channel_release")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(projects.feature.common)
    implementation(projects.feature.splash)
    implementation(projects.feature.login)
    implementation(projects.feature.home)
    implementation(projects.feature.tool)
    implementation(projects.feature.download)
    implementation(projects.feature.user)
    implementation(projects.feature.settings)
    implementation(projects.feature.player)

    implementation(projects.core.analytics)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.download)
    implementation(projects.core.ui)
    implementation(projects.core.data)

    implementation(projects.okdownload.okdownload)

    implementation(libs.androidx.activity.compose) {
        exclude(group = "androidx.lifecycle", module = "lifecycle-viewmodel-ktx")
    }
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)

    implementation(libs.coil.kt)
    implementation(libs.coil.compose)

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.work.runtime.ktx)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)

    baselineProfile(projects.benchmarks)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)

    implementation(libs.sonner)
}
baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
}

// dependencyGuardBaseline
// dependencyGuard
dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}
