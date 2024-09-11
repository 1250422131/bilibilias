plugins {
    alias(libs.plugins.kotlin.dsl)
}

group = "com.imcys.bilibilias.buildlogic"

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.power.assert.gradlePlugin)
    implementation(libs.truth)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidCompose") {
            id = "bilibilias.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "bilibilias.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidJacoco") {
            id = "bilibilias.android.jacoco"
            implementationClass = "AndroidJacocoConventionPlugin"
        }
        register("androidLibrary") {
            id = "bilibilias.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "bilibilias.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidTest") {
            id = "bilibilias.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidRoom") {
            id = "bilibilias.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidLint") {
            id = "bilibilias.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = "bilibilias.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("androidFlavors") {
            id = "bilibilias.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("hilt") {
            id = "bilibilias.hilt"
            implementationClass = "HiltConventionPlugin"
        }
        register("decompose") {
            id = "bilibilias.decompose"
            implementationClass = "DecomposeConventionPlugin"
        }
        register("detekt") {
            id = libs.plugins.bilibilias.detekt.get().pluginId
            implementationClass = "DetektConventionPlugin"
        }
        register("sqlLin") {
            id = "bilibilias.sqlLin"
            implementationClass = "SqlLinConventionPlugin"
        }
        register("composeCompilerReport") {
            id = "bilibilias.compose.compiler.report"
            implementationClass = "ComposeCompilerReportPlugin"
        }
        register("androidOptionsUnitTests") {
            id = "bilibilias.android.testoptions"
            implementationClass = "AndroidTestOptionsUnitTestsConventionPlugin"
        }
    }
}
