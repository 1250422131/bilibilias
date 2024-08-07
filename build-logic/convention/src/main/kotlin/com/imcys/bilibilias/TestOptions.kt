package com.imcys.bilibilias

import com.android.build.api.dsl.CommonExtension

internal fun configureTestOptionsUnitTests(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        testOptions {
            // For Robolectric
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }
}
