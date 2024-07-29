import dev.shreyaspatil.composeCompilerMetricsGenerator.plugin.ComposeCompilerReportExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure

// todo 工作流任务 :app:releaseComposeCompilerHtmlReport
class ComposeCompilerReportPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configure<ComposeCompilerReportExtension>() {
                outputDirectory =
                    rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
                        .map { it.dir("compose-metrics") }.get().asFile
            }
        }
    }
}
