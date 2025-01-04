package com.imcys.bilibilias

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        if (isPropertyValueIsTrue("enableComposeCompilerReportsAndMetrics")) {
            metricsDestination = relativeToRootProject("compose-metrics")
            reportsDestination = relativeToRootProject("compose-reports")
        }

        generateFunctionKeyMetaClasses = true
        includeSourceInformation = true
        includeTraceMarkers = true
        featureFlags = setOf(
            ComposeFeatureFlag.OptimizeNonSkippingGroups,
        )
    }
}
