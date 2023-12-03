dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/public")
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"

include(":convention")
