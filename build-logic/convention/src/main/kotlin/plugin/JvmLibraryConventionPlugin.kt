package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.extension.configureKotlinJvm

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("bilibiliAs.android.lint")
            }
            configureKotlinJvm()
        }
    }
}
