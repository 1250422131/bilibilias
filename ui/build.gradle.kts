plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.bilibilias.compose)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(mediampLibs.mediamp.exoplayer)
            implementation(libs.androidx.core)
        }
        commonMain.dependencies {
            implementation(projects.logic)

            implementation(projects.core.ffmpeg)
            implementation(projects.core.datastore)
            implementation(projects.core.uiPreview)

            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.materialIconsExtended)

            implementation(libs.decompose)
            implementation(libs.decompose.compose)

            implementation(libs.coil.compose)

            implementation(libs.androidx.compose.material3.adaptive)

            implementation(libs.qr.kit)

            implementation(mediampLibs.mediamp.api)
            implementation(libs.settings.ui)
            implementation(libs.lifecycle.coroutines)

            implementation(libs.reorderable)

            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
//            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
//            implementation(libs.androidx.compose.adaptive.navigation3)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.ui"
}