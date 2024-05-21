import com.android.build.api.dsl.ApplicationExtension
import com.imcys.bilibilias.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationBuglyConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
            }
            dependencies {
                add("implementation", libs.findLibrary("tencent.bugly").get())
                add("implementation", libs.findLibrary("umeng-common").get())
                add("implementation", libs.findLibrary("umeng-asms").get())
                add("implementation", libs.findLibrary("umeng-abtest").get())
                val bom = libs.findLibrary("firebase-bom").get()
                add("implementation", platform(bom))
                "implementation"(libs.findLibrary("firebase.analytics").get())
                "implementation"(libs.findLibrary("firebase.performance").get())
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
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