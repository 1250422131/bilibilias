import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.imcys.bilibilias.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("compose") {
            id = libs.plugins.bilibilias.compose.get().pluginId
            implementationClass = "ComposeConventionPlugin"
        }
        register("kmpLibrary") {
            id = libs.plugins.bilibilias.kmp.library.get().pluginId
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.bilibilias.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}