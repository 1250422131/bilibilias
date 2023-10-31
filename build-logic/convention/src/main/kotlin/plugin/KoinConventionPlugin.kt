package plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class KoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyDependencies()
        }
    }

    private fun Project.applyDependencies() {
        // val bom = libs.findLibrary("koin-bom").get()
        // dependencies {
        //     add("implementation", platform(bom))
        //     add("implementation", libs.findLibrary("koin-core").get())
        //     add("implementation", libs.findLibrary("koin-android").get())
        //     // If you need compose library, add it in your module plz.
        // }
    }
}