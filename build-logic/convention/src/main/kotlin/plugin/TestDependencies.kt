package plugin

import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.implementationDefaultTestDependencies(libs: VersionCatalog) {
    add("testImplementation", libs.findLibrary("junit4").get())
    add("androidTestImplementation", libs.findLibrary("androidx-test-espresso-core").get())
    add("androidTestImplementation", libs.findLibrary("androidx-test-ext-junit").get())
}