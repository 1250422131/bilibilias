plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.bilibilias.compose)
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

            implementation(libs.coil.compose)

            implementation(libs.qr.kit)

            implementation(mediampLibs.mediamp.api)
            implementation(libs.settings.ui)

            implementation(libs.reorderable)

            implementation(libs.koin.compose.viewmodel)

            implementation(libs.androidx.compose.material3.adaptive)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.ui"
}