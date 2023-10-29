package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class HiltConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            pluginManager.applyPlugin()
            applyDependencies()
        }
    }

    private fun PluginManager.applyPlugin() {
        apply("dagger.hilt.android.plugin")
        apply("com.google.devtools.ksp")
    }

    private fun Project.applyDependencies() {
        dependencies {
            add("implementation", libs.findLibrary("hilt.android").get())
            add("ksp", libs.findLibrary("hilt.compiler").get())
        }
    }

}