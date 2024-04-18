pluginManagement {
    includeBuild("build-logic")
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
    }
}
rootProject.name = "BILIBILIAS"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":benchmarks")
include(":common")
include(":model_ffmpeg")
include(":tool_log_export")

include(":core:common")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:testing")
include(":core:ui")
include(":core:crash")
include(":feature:splash")
include(":feature:home")
include(":feature:tool")
include(":feature:download")
include(":feature:user")
include(":core:ffmpeg")
