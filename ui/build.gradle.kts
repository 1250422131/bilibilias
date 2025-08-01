plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.bilibilias.compose)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(mediampLibs.mediamp.exoplayer)
        }
        commonMain.dependencies {
            implementation(projects.logic)

            implementation(projects.core.ffmpeg)
            implementation(projects.core.datastore)
            implementation(projects.core.uiPreview)

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

            implementation("me.zhanghai.compose.preference:preference:2.1.0")
            implementation("com.arkivanov.essenty:lifecycle-coroutines:2.5.0")
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.ui"
}