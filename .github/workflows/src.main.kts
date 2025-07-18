#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.5.0")
@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:upload-artifact:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("gradle:actions__setup-gradle:v4")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.UploadArtifact
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.contexts.EnvContext.GITHUB_WORKSPACE
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig

workflow(
    name = "Build",
    on = listOf(Push(branches = listOf("kmp"))),
    sourceFile = __FILE__,
    targetFileName = "build.yml",
    concurrency = Concurrency(
        buildString {
            append("build")
            append("-")
            append(expr { github.ref })
        },
        cancelInProgress = true
    ),
    consistencyCheckJobConfig = ConsistencyCheckJobConfig.Disabled
) {
    job(id = "BuildAndUpload", runsOn = UbuntuLatest, timeoutMinutes = 50) {
        uses(name = "Checkout", action = Checkout())
        run(
            name = "Copy CI gradle.properties",
            command = "mkdir -p ~/.gradle ; cp .github/ci-gradle.properties ~/.gradle/gradle.properties"
        )
        SetupJava.Distribution.Zulu
        uses(
            name = "Setup Java",
            action = SetupJava(
                distribution = SetupJava.Distribution.Zulu,
                javaVersion = "21"
            )
        )
        uses(
            name = "Setup Gradle",
            action = ActionsSetupGradle(
                cacheDisabled = true,
            )
        )
        run(name = "Check build-logic", command = "./gradlew :build-logic:convention:check")
        run(
            name = "Apk Sign",
            command = """
                cp $GITHUB_WORKSPACE/.github/workflows/bilibilias.jks    $GITHUB_WORKSPACE/bilibilias.jks
                echo "signing_release_storeFileFromRoot=./bilibilias.jks" >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_keyAlias=bilibilias'                >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_storePassword=bilibilias'           >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_keyPassword=bilibilias'             >> $GITHUB_WORKSPACE/gradle.properties
                cat "$GITHUB_WORKSPACE/gradle.properties"
            """.trimIndent()
        )
        run(
            name = "Build all build type and flavor permutations",
            command = "./gradlew :app:assemble"
        )
        uses(
            name = "Upload build outputs (APKs)",
            action = UploadArtifact(
                name = "APKs",
                path = listOf("**/build/outputs/apk/**/*.apk")
            )
        )
    }
}