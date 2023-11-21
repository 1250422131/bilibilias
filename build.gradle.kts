@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    // ./gradlew dependencyUpdates
    id("com.github.ben-manes.versions") version "0.50.0"
}
tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        listOf("alpha", "beta", "rc", "cr", "m", "eap", "pr", "dev").any { qualifier ->
            candidate.version.contains(qualifier, ignoreCase = true)
        }
    }
}
