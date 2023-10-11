@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}

ksp {
    arg("ModuleName", project.name)
}

android {
    namespace = "com.imcys.bilibilias"
    compileSdk = 34
    buildToolsVersion = "34.0.0"
    defaultConfig {
        applicationId = "com.imcys.bilibilias"
        targetSdk = 34
        minSdk = 21
        versionCode = 203
        versionName = "2.0.31"

        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        flavorDimensions += project.name
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            resValue("string", "app_name", "@string/app_name_debug")
            resValue("string", "app_channel", "@string/app_channel_debug")
        }

        release {
            applicationIdSuffix = ".release"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
        }
    }

    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }
}

kotlin {
    jvmToolchain(17)
}

kapt {
    correctErrorTypes = true
    useBuildCache = true
}

dependencies {
    implementation(project(":common"))
    implementation(project(":model_ffmpeg"))
    implementation(project(":tool_log_export"))

    implementation(libs.appcompat)
    implementation(libs.constraintlayout)

    implementation(libs.hilt.android)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.compiler)

    ksp(libs.kcomponent.compiler)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)

    implementation(libs.material3)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.navigation.material)
    implementation(libs.material3.window.size)
    implementation(libs.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    debugImplementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    androidTestImplementation(platform(libs.compose.bom))

    /**
     * banner
     */
    implementation(libs.zhujiang.banner)

    /**
     * 网络图片加载库
     */
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    implementation(libs.kotlinx.serialization.protobuf)
    implementation(libs.kotlinx.serialization.cbor)
    /**
     * 下载
     */
    implementation(libs.androidx.xfetch2)
    implementation(libs.compose.settings.ui.m3)
    /**
     * 文件选择
     */
    implementation("com.darkrockstudios:mpfilepicker:2.1.0")
}
