pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven("https://mirrors.tencent.com/nexus/repository/gradle-plugins/")
        mavenCentral()
        maven("https://jitpack.io")
        google()
    }
}
rootProject.name = "BILIBILIAS"
include(":app")
include(":common")
include(":model_ffmpeg")
include(":tool_log_export")
