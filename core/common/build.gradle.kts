import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.bilibilias.kmp.library)
    alias(libs.plugins.bilibilias.compose)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.startup)
            implementation(libs.androidx.core)
        }
        commonMain.dependencies {
            api(compose.runtime)

            api(libs.androidx.datastore)
            implementation("co.touchlab:kermit:2.0.4")

            api(libs.kotlinx.datetime)
            api(libs.kotlinx.io)
            api(libs.kotlinx.coroutines.core)

            api(libs.kotlinx.serialization.json)

            api(libs.uri.kmp)
        }
    }
}

android {
    namespace = "com.imcys.bilibilias.core.common"
}

buildkonfig {
    packageName = "com.imcys.bilibilias.mp"

    // default config is required
    defaultConfigs {}
    defaultConfigs("debug") {
        buildConfigField(STRING, "packageName", packageName)
        buildConfigField(BOOLEAN, "debugBuild", "true")
    }
    // flavor is passed as a first argument of defaultConfigs
    defaultConfigs("release") {
        buildConfigField(STRING, "name", "devValue")
        buildConfigField(BOOLEAN, "debugBuild", "false")
    }
}
