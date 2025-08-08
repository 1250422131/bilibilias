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
        }
        commonMain.dependencies {
            api(compose.runtime)

            api(libs.androidx.datastore)
            
            api(libs.kermit)
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
    packageName = "com.imcys.bilibilias"

    // default config is required
    defaultConfigs {
        buildConfigField(STRING, "packageName", packageName)
        buildConfigField(BOOLEAN, "debugBuild", "false")
    }
    // flavor is passed as a first argument of defaultConfigs
    defaultConfigs("dev") {
        buildConfigField(STRING, "name", "devValue")
        buildConfigField(BOOLEAN, "debugBuild", "true")
    }

    targetConfigs {
        create("android") {
            buildConfigField(STRING, "name2", "value2")
        }
    }
}
