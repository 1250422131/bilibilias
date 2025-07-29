import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                sourceSets.named("commonMain").configure {
                    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
                }
            }
            dependencies {
                "commonMainImplementation"(libs.findLibrary("koin.core").get())
                "commonMainImplementation"(libs.findLibrary("koin.annotations").get())
                "kspCommonMainMetadata"(libs.findLibrary("koin.ksp.compiler").get())
                "kspAndroid"(libs.findLibrary("koin.ksp.compiler").get())
                "kspDesktop"(libs.findLibrary("koin.ksp.compiler").get())
            }
            tasks.withType(KotlinCompilationTask::class.java).configureEach {
                if (name != "kspCommonMainKotlinMetadata") {
                    dependsOn("kspCommonMainKotlinMetadata")
                }
            }
        }
    }
}