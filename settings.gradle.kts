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
rootProject.name = "bilibilias"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

include(":common")
include(":tool_log_export")

// include(":app-nia-catalog")
include(":benchmarks")
// include(":core:analytics")
include(":core:common")
include(":core:crash")
// include(":core:data")
// include(":core:data-test")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
// include(":core:datastore-test")
include(":core:designsystem")
include(":core:domain")
include(":core:download")
include(":core:ffmpeg")
include(":core:model")
include(":core:network")
include(":core:testing")
include(":core:ui")
// include(":core:notifications")
// include(":core:screenshot-testing")

include(":okdownload:okdownload")
include(":okdownload:okdownload-connection-okhttp")

include(":feature:home")
include(":feature:tool")
include(":feature:download")
 include(":feature:user")
include(":feature:splash")
// include(":feature:settings")
// include(":lint")
// include(":sync:work")
// include(":sync:sync-test")
// include(":ui-test-hilt-manifest")
