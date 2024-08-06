import org.gradle.api.Plugin
import org.gradle.api.Project

// https://patilshreyas.github.io/compose-report-to-html/use/using-cli/
// todo 工作流任务 :app:releaseComposeCompilerHtmlReport
class ComposeCompilerReportPlugin : Plugin<Project> {
    override fun apply(target: Project) {
//        with(target) {
//            configure<ComposeCompilerReportExtension> {
//                outputDirectory =
//                    rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
//                        .map { it.dir("compose-metrics") }.get().asFile
//            }
//        }
    }
}
