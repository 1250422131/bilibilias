package plugin

import AndroidConfigConventions
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin(
    commonExtension: CommonExtension<*, *, *, *, *>
) {
    with(commonExtension) {
        compileOptions {
            sourceCompatibility = AndroidConfigConventions.JAVA_VERSION
            targetCompatibility = AndroidConfigConventions.JAVA_VERSION
        }
        kotlinOptions {
            jvmTarget = AndroidConfigConventions.JAVA_VERSION.toString()
            freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
                addAll(
                    listOf(
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
                    )
                )
                // open Kotlin context feature
                add("-Xcontext-receivers")
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            // JVM 17 not support sealed class, so use JVM 11 yet.
            jvmTarget.set(JvmTarget.fromTarget(AndroidConfigConventions.JAVA_VERSION.toString()))
        }
    }

}

internal fun CommonExtension<*, *, *, *, *>.configureAndroidCommon() {
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/*"
            excludes += "DebugProbesKt.bin"
            // excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}