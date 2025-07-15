plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.bilibilias.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.logic)

            implementation(projects.core.mediaCache)
            implementation(projects.core.uiPreview)
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.materialIconsExtended)

            implementation(libs.decompose)
            implementation(libs.decompose.compose)

            implementation(libs.coil.compose)

            implementation("org.jetbrains.compose.material3.adaptive:adaptive:1.1.2")
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.ui"
}