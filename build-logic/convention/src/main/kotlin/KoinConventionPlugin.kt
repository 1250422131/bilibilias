import com.google.devtools.ksp.gradle.KspExtension
import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            extensions.configure(KspExtension::class.java) {
                arg("KOIN_CONFIG_CHECK", "true")
            }
            dependencies {
                "commonMainImplementation"(libs.findLibrary("koin.core").get())
            }
        }
    }
}