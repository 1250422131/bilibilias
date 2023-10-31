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
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
        mavenCentral()
        google()
    }
}
rootProject.name = "BILIBILIAS"
include(":app")
include(":common")
include(":feature:tool")
include(":core:common")
include(":core:ui")
include(":core:design-system")
include(":core:network")
include(":core:model")
