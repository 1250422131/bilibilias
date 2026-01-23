import com.imcys.bilibilias.buildlogic.BILIBILIASBuildType

plugins {
    alias(libs.plugins.bilibilias.android.application)
    alias(libs.plugins.bilibilias.android.koin)
    alias(libs.plugins.bilibilias.baidu.jar)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias { libs.plugins.kotlin.parcelize }
}
val enabledPlayAppMode: String by project
val enabledAnalytics: String by project
val baiduStatId: String = project.findProperty("as.baidu.stat.id")?.toString() ?: ""

android {
    namespace = "com.imcys.bilibilias"

    defaultConfig {
        targetSdk = 36
        applicationId = "com.imcys.bilibilias"
        versionCode = 316
        versionName = "3.1.6"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["BAIDU_STAT_ID"] = baiduStatId
        buildConfigField("String", "BAIDU_STAT_ID", """"$baiduStatId"""".trimIndent())
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a","x86_64")
        }
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
            buildConfigField("boolean", "ENABLED_PLAY_APP_MODE", enabledPlayAppMode)
            signingConfig = signingConfigs.getByName("BILIBILIASSigningConfig")
            resValue("string", "app_channel", "Official")
        }

        create("alpha") {
            dimension = "version"
            applicationIdSuffix = BILIBILIASBuildType.ALPHA.applicationIdSuffix
            versionNameSuffix = BILIBILIASBuildType.ALPHA.versionNameSuffix
            buildConfigField("boolean", "ENABLED_PLAY_APP_MODE", "false")
            resValue("string", "app_channel", "Alpha")
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
            buildConfigField("boolean", "ENABLED_PLAY_APP_MODE", enabledPlayAppMode)
            signingConfig = signingConfigs.getByName("BILIBILIASSigningConfig")
            resValue("string", "app_channel", "Beta")

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
            buildConfigField("boolean", "ENABLED_PLAY_APP_MODE", enabledPlayAppMode)
            buildConfigField("boolean", "ENABLED_ANALYTICS", enabledAnalytics)
        }

        debug {
            buildConfigField("boolean", "ENABLED_PLAY_APP_MODE", enabledPlayAppMode)
            buildConfigField("boolean", "ENABLED_ANALYTICS", enabledAnalytics)
        }

    }

    val isDebug = gradle.startParameter.taskNames.any { it.contains("debug", true) }
    splits {
        abi {
            isEnable = !isDebug  // debug 时禁用，release 时启用
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86_64")
            isUniversalApk = true
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
        resValues = true
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-XXLanguage:+ExplicitBackingFields")
        }
    }

    if (!enabledPlayAppMode.toBoolean() && enabledAnalytics.toBoolean()) {
        /**
         * 百度统计静态清单合并
         */
        androidComponents {
            onVariants { variant ->
                variant.sources.manifests.addStaticManifestFile("src/baidu/AndroidManifest.xml")
            }
        }
    }

}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))

    implementation(libs.ffmpeg.kit.x6kb)

    // Firebase 选配
    firebaseDependencies(enabledAnalytics.toBoolean())

    // 彩带
    implementation(libs.konfetti.compose)
    // 高斯模糊
    implementation(libs.compose.cloudy)

    // 分页
    implementation(libs.paging.compose)

    implementation(libs.device.compat)
    implementation(libs.androidx.documentfile)

    // Google Play 选配
    googlePlayDependencies(enabledPlayAppMode.toBoolean())

    // 百度统计
    baiduStatDependencies()

    // Shizuku
    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)

    // xposed
    //    compileOnly(libs.xposed.api)


    // 预览工具
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}

// 百度统计依赖配置
fun DependencyHandlerScope.baiduStatDependencies() {
    val baiduJar = fileTree("libs") { include("Baidu_Mtj_android_*.jar") }
    if (!baiduJar.isEmpty) {
        if (enabledAnalytics.toBoolean() && !enabledPlayAppMode.toBoolean()) {
            implementation(baiduJar)
        } else {
            compileOnly(baiduJar)
        }
    }
}
// Google Play 依赖配置
fun DependencyHandlerScope.googlePlayDependencies(enabled: Boolean) {
    val googlePlayLibs = listOf(
        libs.play.app.update.kts,
        libs.play.app.review.kts
    )
    googlePlayLibs.forEach {
        if (enabled) {
            implementation(it)
        } else {
            compileOnly(it)
        }
    }
}


// Firebase 依赖配置
fun DependencyHandlerScope.firebaseDependencies(enabled: Boolean) {
    if (enabled) {
        implementation(platform(libs.firebase.bom))
        implementation(libs.firebase.crashlytics)
        implementation(libs.firebase.crashlytics.ndk)
        implementation(libs.firebase.analytics)
        implementation(libs.firebase.config)
        implementation(libs.firebase.messaging)
        implementation(libs.firebase.inappmessaging.display) {
            exclude(group = "com.google.firebase", module = "protolite-well-known-types")
        }
        implementation(libs.firebase.perf) {
            exclude(group = "com.google.protobuf", module = "protobuf-javalite")
            exclude(group = "com.google.firebase", module = "protolite-well-known-types")
        }
    } else {
        compileOnly(platform(libs.firebase.bom))
        compileOnly(libs.firebase.crashlytics)
        compileOnly(libs.firebase.crashlytics.ndk)
        compileOnly(libs.firebase.analytics)
        compileOnly(libs.firebase.config)
        compileOnly(libs.firebase.messaging)
        compileOnly(libs.firebase.inappmessaging.display)
        compileOnly(libs.firebase.perf)
    }
}