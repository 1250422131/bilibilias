dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(" https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
