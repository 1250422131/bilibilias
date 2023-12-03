includeBuild("build-logic")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
        maven("https://androidx.dev/storage/compose-compiler/repository/")
        google()
    }
}
rootProject.name = "BILIBILIAS"
include(":app")
include(":common")

include(":core:common")
include(":core:ui")
include(":core:design-system")
include(":core:network")
include(":core:model")
include(":core:database")
include(":core:datastore")

include(":feature:authentication")
include(":feature:home")
include(":feature:tool")
include(":feature:user")
include(":feature:setting")
include(":feature:player")

include(":lint")
include(":core:okdownloader")
include(":core:datastore-proto")
