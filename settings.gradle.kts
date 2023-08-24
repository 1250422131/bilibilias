pluginManagement {
    repositories {
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/public")
        mavenCentral()

        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        google()

    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/public")
        mavenCentral()
        
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")

        google()

    }
}
rootProject.name = "BILIBILIAS"
include(":app")
include(":common")
include(":model_ffmpeg")
include(":tool_log_export")
