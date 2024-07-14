package com.imcys.bilibilias

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

internal fun Project.configureTestOptionsUnitTests(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        testOptions {
            // For Robolectric
            unitTests {
                isIncludeAndroidResources = true
            }
        }
    }
}
