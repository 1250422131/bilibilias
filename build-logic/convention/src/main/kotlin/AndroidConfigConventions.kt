@file:Suppress("unused")

import org.gradle.api.JavaVersion

object AndroidConfigConventions {
    val JAVA_VERSION = JavaVersion.VERSION_17

    object BilibiliAS {
        private const val PACKAGE_NAME = "com.imcys.bilibilias"
        const val APPLICATION_ID = PACKAGE_NAME

        const val VERSION_CODE = 203
        const val VERSION_NAME = "2.0.31"
    }
}