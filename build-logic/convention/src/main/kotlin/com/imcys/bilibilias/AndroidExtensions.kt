package com.imcys.bilibilias

import org.gradle.api.Project

val Project.androidCompileSdk: Int
    get() = libs.findVersion("android-compileSdk").get().toString().toInt()

val Project.androidMinSdk: Int
    get() = libs.findVersion("android-minSdk").get().toString().toInt()

val Project.androidTargetSdk
    get() = libs.findVersion("android-targetSdk").get().toString().toInt()