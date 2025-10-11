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
        versionCode = 302
        versionName = "3.0.0-PlumBlossom-8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("BILIBILIASSigningConfig") {
            enableV3Signing = true
            enableV4Signing = true
        }
    }

    flavorDimensions += listOf("version")
    productFlavors {

        create("official") {
            dimension = "version"
            buildConfigField("boolean", "ENABLE_PLAY_APP_MODE", enablePlayAppMode)
            signingConfig = signingConfigs.getByName("BILIBILIASSigningConfig")
        }

        create("alpha") {
            dimension = "version"
            applicationIdSuffix = BILIBILIASBuildType.ALPHA.applicationIdSuffix
            versionNameSuffix = BILIBILIASBuildType.ALPHA.versionNameSuffix
            buildConfigField("boolean", "ENABLE_PLAY_APP_MODE", "false")
            // 动态签名配置
            val runnerTemp = System.getenv("RUNNER_TEMP")
            signingConfig = if (runnerTemp != null && file("$runnerTemp/mxjs-debug.jks").exists()) {
                // CI 环境
                signingConfigs.create("ci-alpha").apply {
                    storeFile = file("$runnerTemp/mxjs-debug.jks")
                    storePassword = System.getenv("ALPHA_KEYSTORE_PASSWORD")
                    keyAlias = System.getenv("ALPHA_KEY_ALIAS")
                    keyPassword = System.getenv("ALPHA_KEY_PASSWORD")
                    enableV3Signing = true
                    enableV4Signing = true
                }
            } else {
                // 本地环境
                signingConfigs.getByName("debug")
            }

        }

        // 提交Google Play使用
        create("beta") {
            dimension = "version"
            applicationIdSuffix = BILIBILIASBuildType.BETA.applicationIdSuffix
            versionNameSuffix = BILIBILIASBuildType.BETA.versionNameSuffix
            buildConfigField("boolean", "ENABLE_PLAY_APP_MODE", enablePlayAppMode)
            signingConfig = signingConfigs.getByName("BILIBILIASSigningConfig")
        }
    }

    buildTypes {
        release {
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
    implementation(libs.firebase.perf) {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    // 彩带
    implementation(libs.konfetti.compose)
    // 高斯模糊
    implementation(libs.compose.cloudy)

    // 分页
    implementation(libs.paging.compose)

    implementation(libs.device.compat)

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

    // Shizuku
    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)

    // xposed
    compileOnly(libs.xposed.api)

    // 预览工具
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}
