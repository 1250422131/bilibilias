package com.imcys.bilibilias

import com.android.build.api.dsl.CommonExtension

internal fun configureResourcesPackaging(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
                merges += "META-INF/LICENSE.md"
                merges += "META-INF/LICENSE-notice.md"
            }
        }
    }
}
