import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class MultiplatformSqlLinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("implementation", libs.findLibrary("sqllin.dsl").get())
                add("implementation", libs.findLibrary("sqllin.driver").get())
                add("ksp", libs.findLibrary("sqllin.processor").get())

                add("implementation", libs.findLibrary("kotlinx.serialization.core").get())
                add("implementation", libs.findLibrary("kotlinx.coroutines.core").get())
            }
        }
    }
}