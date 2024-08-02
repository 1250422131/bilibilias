pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        google()
    }
}
rootProject.name = "bilibilias"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")

include(":benchmarks")
include(":core:analytics")
include(":core:common")
include(":core:crash")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:designsystem")
include(":core:domain")
include(":core:download")
include(":core:ffmpeg")
include(":core:model")
include(":core:network")
include(":core:screenshot-testing")
include(":core:testing")
include(":core:ui")

include(":feature:authorspace")
include(":feature:common")
include(":feature:download")
include(":feature:home")
include(":feature:login")
include(":feature:player")
include(":feature:settings")
include(":feature:splash")
include(":feature:tool")
include(":feature:user")
include(":feature:ffmpeg-action")

include(":lint")

include(":okdownload:okdownload")

include(":ui-test-hilt-manifest")
include(":sync:work")
