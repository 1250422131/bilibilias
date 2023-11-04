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
        mavenCentral()
        maven("https://jitpack.io")
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
