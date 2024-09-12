import com.imcys.bilibilias.AsBuildType

plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.compose)
    alias(libs.plugins.bilibilias.android.jacoco)
    alias(libs.plugins.bilibilias.android.application.flavors)
    alias(libs.plugins.bilibilias.android.testoptions)
    alias(libs.plugins.bilibilias.decompose)
    alias(libs.plugins.bilibilias.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.buildconfig)
}

android {
    namespace = "com.imcys.bilibilias"
    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        versionCode = 22
        versionName = "0.1.22"
        ndk {
            abiFilters += listOf("arm64-v8a")
        }

        testInstrumentationRunner = "com.imcys.bilibilias.core.testing.AsTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        applicationVariants.all {
            outputs.forEach { output ->
                if (output is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                    output.outputFileName =
                        "bilibilias-v$versionName-$name.${output.outputFile.extension}"
                }
            }
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = AsBuildType.DEBUG.applicationIdSuffix
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
            applicationIdSuffix = AsBuildType.RELEASE.applicationIdSuffix
            baselineProfile.automaticGenerationDuringBuild = true
            signingConfig = signingConfigs["BilibiliAsSigningConfig"]
            resValue("string", "app_name", "BILIBILIAS")
            resValue("string", "app_channel", "Official Channel")
        }
    }
    packaging {
        jniLibs {
            pickFirsts += "**/libavcodec.so"
            pickFirsts += "**/libavutil.so"
            pickFirsts += "**/libswresample.so"
            pickFirsts += "**/libswscale.so"
            pickFirsts += "**/libavformat.so"
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
    implementation(projects.feature.authorspace)
    implementation(projects.feature.ffmpegAction)

    implementation(projects.core.analytics)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.download)
    implementation(projects.core.ui)
    implementation(projects.core.data)

    implementation(projects.sync.work)
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
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)

    implementation(libs.coil.kt)
    implementation(libs.coil.compose)

    implementation(libs.androidx.work.runtime)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.dokar.sonner)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(projects.uiTestHiltManifest)

    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.hilt.android.testing)
    testImplementation(testFixtures(projects.core.data))

    testDemoImplementation(libs.robolectric)
    testDemoImplementation(libs.roborazzi)
    testDemoImplementation(projects.core.screenshotTesting)
    testDemoImplementation(testFixtures(projects.core.data))

    androidTestImplementation(kotlin("test"))
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(testFixtures(projects.core.data))
    androidTestImplementation(testFixtures(projects.core.datastore))

    baselineProfile(projects.benchmarks)
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
