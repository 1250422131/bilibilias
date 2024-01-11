import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "bilibiliAs.android.application.compose"
            implementationClass = "plugin.AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "bilibiliAs.android.application"
            implementationClass = "plugin.AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "bilibiliAs.android.library.compose"
            implementationClass = "plugin.AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "bilibiliAs.android.library"
            implementationClass = "plugin.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "bilibiliAs.android.feature"
            implementationClass = "plugin.AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "bilibiliAs.android.hilt"
            implementationClass = "plugin.AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "bilibiliAs.android.room"
            implementationClass = "plugin.AndroidRoomConventionPlugin"
        }
        register("androidLint") {
            id = "bilibiliAs.android.lint"
            implementationClass = "plugin.AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = "bilibiliAs.jvm.library"
            implementationClass = "plugin.JvmLibraryConventionPlugin"
        }
    }
}
