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
        register("androidHilt") {
            id = "bilibilias.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "bilibilias.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("multiplatformSqlLin") {
            id = "bilibilias.multiplatform.sqlLin"
            implementationClass = "MultiplatformSqlLinConventionPlugin"
        }
        register("androidLint") {
            id = "bilibilias.android.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = "bilibilias.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("multiplatformDecompose") {
            id = "bilibilias.multiplatform.decompose"
            implementationClass = "MultiplatformDecomposeConventionPlugin"
        }
        register("androidFlavors") {
            id = "bilibilias.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("androidBugly") {
            id = "bilibilias.android.application.bugly"
            implementationClass = "AndroidApplicationBuglyConventionPlugin"
        }
        register("androidApplicationTestOptionsUnitTests") {
            id = "bilibilias.android.testoptions"
            implementationClass = "AndroidTestOptionsUnitTestsConventionPlugin"
        }
        register("androidDetekt") {
            id = "bilibilias.kotlin.detekt"
            implementationClass = "KotlinDetektConventionPlugin"
        }
    }
}
