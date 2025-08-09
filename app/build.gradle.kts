import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.properties.hasProperty
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.koin.androidx.workmanager)
        }
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.datastore)
            implementation(projects.core.httpDownloader)
            implementation(projects.core.ktorClient)
            implementation(projects.logic)
            implementation(projects.ui)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.coil)
            implementation(libs.coil.network.ktor)

            implementation(libs.decompose)

            implementation(libs.koin.core)
            implementation(libs.koin.core.coroutines)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.imcys.bilibilias.mp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 30000
        versionName = "3.0.1"
        ndk {
            abiFilters += "arm64-v8a"
        }
    }
    signingConfigs {
        val file = File(rootProject.file("gradle.properties").path)
        val properties = file.loadProperties()
        if (properties.hasProperty("signing_release_storeFileFromRoot")) {
            create("release") {
                storeFile =
                    rootProject.file(properties.getProperty("signing_release_storeFileFromRoot"))
                storePassword = properties.getProperty("signing_release_storePassword")
                keyAlias = properties.getProperty("signing_release_keyAlias")
                keyPassword = properties.getProperty("signing_release_keyPassword")
            }
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/native-image/*/jnijavacpp/*"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.findByName("release")
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "debug"
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

compose.desktop {
    application {
        mainClass = "com.imcys.bilibilias.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.imcys.bilibilias"
            packageVersion = "1.0.0"
        }
    }
}
