package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class RoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugin()
            applyDependencies()
        }
    }

    private fun PluginManager.applyPlugin() {
        apply("com.google.devtools.ksp")
    }

    private fun Project.applyDependencies() {
        dependencies {
            add("implementation", libs.findLibrary("room.runtime").get())
            add("implementation", libs.findLibrary("room.ktx").get())
            add("ksp", libs.findLibrary("room.compiler").get())
        }
    }
}