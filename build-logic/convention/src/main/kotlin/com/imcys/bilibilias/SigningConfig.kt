package com.imcys.bilibilias

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

internal fun Project.configureSigning(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val environment = System.getenv()
    fun getLocalProperty(key: String): String? {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        if (keystorePropertiesFile.exists().not()) {
            return null
        }
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        return keystoreProperties[key] as String?
    }

    fun String.toFile() = File(this)
    commonExtension.signingConfigs {
        create("BilibiliAsSigningConfig") {
            keyAlias = getLocalProperty("signing.keyAlias") ?: environment["ALIAS"]
            storeFile =
                (
                    getLocalProperty("signing.storeFile")
                        ?: environment["SIGNING_STORE_FILE"]
                    )?.toFile()
            keyPassword =
                getLocalProperty("signing.keyPassword") ?: environment["KEY_PASSWORD"]
            storePassword =
                getLocalProperty("signing.storePassword") ?: environment["KEY_STORE_PASSWORD"]
            enableV3Signing = true
            enableV4Signing = true
        }
    }
}
