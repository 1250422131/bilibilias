import com.imcys.bilibilias.buildlogic.BILIBILIASBuildType

plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.koin)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.kotlin.plugin.serialization)

}
val enablePlayAppMode: String by project

android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        targetSdk = 36
        applicationId = "com.imcys.bilibilias"
        versionCode = 300
        versionName = "3.0.0-PlumBlossom-Alpha-5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {
            applicationIdSuffix = BILIBILIASBuildType.RELEASE.applicationIdSuffix
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "ENABLE_PLAY_APP_MODE", enablePlayAppMode)
        }

        debug {
            buildConfigField("boolean", "ENABLE_PLAY_APP_MODE", enablePlayAppMode)
        }

        // 提交Google Play使用
        create("beta") {
            initWith(getByName("release"))
            applicationIdSuffix = BILIBILIASBuildType.BETA.applicationIdSuffix
            versionNameSuffix = BILIBILIASBuildType.BETA.versionNameSuffix
            buildConfigField("boolean", "ENABLE_PLAY_APP_MODE", enablePlayAppMode)
        }

        // GitHub Action 打包使用
        create("alpha") {
            initWith(getByName("release"))
            applicationIdSuffix = BILIBILIASBuildType.ALPHA.applicationIdSuffix
            versionNameSuffix = BILIBILIASBuildType.ALPHA.versionNameSuffix
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField(
                "boolean", "ENABLE_PLAY_APP_MODE", "false"
            )
        }

    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-XXLanguage:+WhenGuards")
        }
    }
}

dependencies {


    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:ffmpeg"))


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.inappmessaging.display) {
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }
    implementation(libs.firebase.config)
    implementation(libs.firebase.messaging)
//    implementation(libs.firebase.config.ktx)
    implementation(libs.firebase.perf) {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    // 彩带
    implementation(libs.konfetti.compose)
    // 高斯模糊
    implementation(libs.compose.cloudy)

    // 启动屏
    // implementation(libs.androidx.core.splashscreen)

    // 分页
    implementation(libs.paging.compose)

    // Google Play 选配
    val googlePlayLibs = listOf(
        libs.palay.app.update.kts,
        libs.palay.app.review.kts
    )
    googlePlayLibs.forEach {
        if (enablePlayAppMode.toBoolean()) {
            implementation(it)
        } else {
            compileOnly(it)
        }
    }


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
