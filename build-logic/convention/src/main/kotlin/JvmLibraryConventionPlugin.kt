import com.imcys.bilibilias.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("bilibilias.android.lint")
            }
            configureKotlin<KotlinJvmProjectExtension>()
        }
    }
}
