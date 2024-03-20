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
        mavenCentral()
        maven("https://jitpack.io")
        google()
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}
rootProject.name = "BILIBILIAS"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":common")
include(":model_ffmpeg")
include(":tool_log_export")
include(":core:model")
