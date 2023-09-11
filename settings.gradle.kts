pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.gitlab.arturbosch.detekt") {
                useModule("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${requested.version}")
            }
        }
    }
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://jitpack.io")
        mavenCentral()
        google()
    }
}
rootProject.name = "BILIBILIAS"
include(":app")
include(":common")
include(":model_ffmpeg")
include(":tool_log_export")
