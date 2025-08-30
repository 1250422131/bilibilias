package com.imcys.bilibilias.core.context

import com.imcys.bilibilias.mp.BuildKonfig

object BuildConfig {
    val packageName: String = BuildKonfig.packageName

    val debugBuild: Boolean = BuildKonfig.debugBuild
}