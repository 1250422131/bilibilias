import com.android.build.api.dsl.ApplicationExtension
import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationBuglyConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", libs.findLibrary("tencent.bugly").get())
                add("implementation", libs.findLibrary("umeng-common").get())
                add("implementation", libs.findLibrary("umeng-asms").get())
                add("implementation", libs.findLibrary("umeng-abtest").get())
            }

            extensions.configure<ApplicationExtension> {
                buildTypes.configureEach {
                    // Disable the Crashlytics mapping file upload. This feature should only be
                    // enabled if a Firebase backend is available and configured in
                    // google-services.json.
//                    configure<CrashlyticsExtension> {
//                        mappingFileUploadEnabled = false
//                    }
                }
            }
        }
    }
}
