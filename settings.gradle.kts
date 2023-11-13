pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
        google()
    }
}
rootProject.name = "BILIBILIAS"
include(":app")
include(":common")
include(":model_ffmpeg")
include(":tool_log_export")
