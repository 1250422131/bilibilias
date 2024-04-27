pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
        maven("https://mirrors.tencent.com/nexus/repository/gradle-plugins/")
        mavenCentral()
        maven("https://jitpack.io")
        google()
        // 临时为Kotlin提供对应的compose-compiler支持，详细见仓库：
        // https://github.com/jimgoog/ComposeAppUsingPrereleaseComposeCompiler#project-configuration
        maven("https://androidx.dev/storage/compose-compiler/repository/")
    }
}

val osName = System.getProperty("os.name").toLowerCase()

val projectPrefix = when {
    osName.contains("win") -> "BILIBILIAS"
    else -> "bilibilias"
}

rootProject.name = projectPrefix
include(":app")
include(":common")
include(":model_ffmpeg")
include(":tool_log_export")
