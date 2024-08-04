dependencyResolutionManagement {
    repositories {
        maven("https://dl.google.com/dl/android/maven2/")
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
