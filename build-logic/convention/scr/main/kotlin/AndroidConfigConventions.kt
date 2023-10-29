@file:Suppress("unused")

import org.gradle.api.JavaVersion

object AndroidConfigConventions {
    private const val SDK_VERSION = 34
    const val MIN_SDK_VERSION = 24
    const val COMPILE_SDK_VERSION = SDK_VERSION
    const val TARGET_SDK_VERSION = SDK_VERSION

    val JAVA_VERSION = JavaVersion.VERSION_17

    object LittleGoose {
        private const val PACKAGE_NAME = "com.imcys.bilibilias"
        const val APPLICATION_ID = PACKAGE_NAME

        const val VERSION_CODE = 203
        const val VERSION_NAME = "2.0.31"
    }

}