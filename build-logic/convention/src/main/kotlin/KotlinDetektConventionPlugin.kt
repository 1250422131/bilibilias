import com.imcys.bilibilias.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

// detektGenerateConfig
// detektBaseline
class KotlinDetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "io.gitlab.arturbosch.detekt")
            configure<DetektExtension> {
                config.setFrom(file("$rootDir/config/detekt.yml"))
                basePath = rootDir.absolutePath
                autoCorrect = true
                tasks.named<Detekt>("detekt") {
                    reports {
                        xml.required.set(true)
                        html.required.set(true)
                        txt.required.set(true)
                        sarif.required.set(true)
                        md.required.set(true)
                    }
                }
            }
            dependencies {
                "detektPlugins"(libs.findLibrary("detekt-formatting").get())
                "detektPlugins"(libs.findLibrary("detekt-compose-rules").get())
            }
        }
    }
}
