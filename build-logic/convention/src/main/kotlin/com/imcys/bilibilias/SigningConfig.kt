package com.imcys.bilibilias

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

internal fun Project.configureSigning(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    val tmpFilePath = System.getProperty("user.home") + "/work/_temp/keystore/"
    val allFilesFromDir = file(tmpFilePath).listFiles()

    if (allFilesFromDir != null) {
        val keystoreFile = allFilesFromDir.first()
        keystoreFile.renameTo(file("keystore/keystore.jks"))
    }
    fun getLocalProperty(key: String): String? {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        if (!keystorePropertiesFile.exists()) {
            return null
        }
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        return keystoreProperties[key] as String?
    }

    fun String.toFile() = File(this)
    commonExtension.signingConfigs {
        create("BilibiliAsSigningConfig") {
            val environment = System.getenv()
            keyAlias = getLocalProperty("signing.keyAlias") ?: environment["SIGNING_KEY_ALIAS"]
            storeFile =
                getLocalProperty("signing.storeFile")?.toFile() ?: file("home/runner/work/_temp/keystore.jks")
            keyPassword =
                getLocalProperty("signing.keyPassword") ?: environment["SIGNING_KEY_PASSWORD"]
            storePassword =
                getLocalProperty("signing.storePassword") ?: environment["SIGNING_STORE_PASSWORD"]
            enableV3Signing = true
            enableV4Signing = true
        }
    }
}