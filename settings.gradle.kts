pluginManagement {
    repositories {
        includeBuild("build-logic")
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/")
        mavenCentral()
        google()
        maven("https://jitpack.io")
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}
rootProject.name = "BILIBILIAS"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":common")

include(":core:common")
include(":core:ui")
include(":core:designsystem")
include(":core:network")
include(":core:model")
include(":core:database")
include(":core:datastore")
include(":core:crash")
include(":core:okdownloader")
include(":core:datastore-proto")
include(":core:domain")

include(":feature:authentication")
include(":feature:home")
include(":feature:tool")
include(":feature:user")
include(":feature:setting")
include(":feature:player")
include(":feature:userspace")
include(":feature:merge")
include(":feature:download")

include(":lint")

include(":XXPermissions:library")
include(":core:testing")
