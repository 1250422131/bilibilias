#!/usr/bin/env kotlin

@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.5.0")
@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")
@file:DependsOn("actions:upload-artifact:v4")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("gradle:actions__setup-gradle:v4")
@file:DependsOn("dawidd6:action-get-tag:v1")
@file:DependsOn("softprops:action-gh-release:v2")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.UploadArtifact
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.actions.softprops.ActionGhRelease
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.contexts.EnvContext.GITHUB_WORKSPACE
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig

workflow(
    name = "Build",
    on = listOf(Push(branches = listOf("kmp"))),
    sourceFile = __FILE__,
    targetFileName = "Build.yml",
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
                echo -e '\n'                                              >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_storeFileFromRoot=./bilibilias.jks' >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_keyAlias=bilibilias'                >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_storePassword=bilibilias'           >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_keyPassword=bilibilias'             >> $GITHUB_WORKSPACE/gradle.properties
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
workflow(
    name = "GitHub Release with APKs",
    permissions = mapOf(
        Permission.Actions to Mode.Write,
        Permission.Contents to Mode.Write,
    ),
    on = listOf(
        WorkflowDispatch(),
        Push(tags = listOf("v*"))
    ),
    sourceFile = __FILE__,
    targetFileName = "Release.yml",
    consistencyCheckJobConfig = ConsistencyCheckJobConfig.Disabled
) {
    job(id = "BuildAndUpload", runsOn = UbuntuLatest, timeoutMinutes = 50) {
        uses(name = "Checkout", action = Checkout())
        run(
            name = "Copy CI gradle.properties",
            command = "mkdir -p ~/.gradle ; cp .github/ci-gradle.properties ~/.gradle/gradle.properties"
        )
        setupJava()
        uses(
            name = "Setup Gradle",
            action = ActionsSetupGradle(),
        )
        run(name = "Check build-logic", command = "./gradlew :build-logic:convention:check")
        run(
            name = "Apk Sign",
            command = """
                cp $GITHUB_WORKSPACE/.github/workflows/bilibilias.jks    $GITHUB_WORKSPACE/bilibilias.jks
                echo -e '\n'                                              >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_storeFileFromRoot=./bilibilias.jks' >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_keyAlias=bilibilias'                >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_storePassword=bilibilias'           >> $GITHUB_WORKSPACE/gradle.properties
                echo 'signing_release_keyPassword=bilibilias'             >> $GITHUB_WORKSPACE/gradle.properties
            """.trimIndent()
        )
        run(
            name = "Build all build type and flavor permutations",
            command = "./gradlew :app:assembleRelease"
        )

        uses(
            name = "Create Release",
            action = ActionGhRelease(
                tagName = expr { github.ref },
                name = expr { github.ref },
                draft = true,
                prerelease = false,
                files = listOf(),
            ),
            env = mapOf("GITHUB_TOKEN" to expr { secrets.GITHUB_TOKEN }),
        )
        uses(
            name = "Upload app",
            action = UploadArtifact(
                name = "APKs",
                path = listOf("**/build/outputs/apk/**/*.apk"),
            ),
            env = mapOf("GITHUB_TOKEN" to expr { secrets.GITHUB_TOKEN }),
        )
    }
}
fun JobBuilder<*>.setupJava() {
    uses(
        name = "Setup Java",
        action = SetupJava(
            distribution = SetupJava.Distribution.Zulu,
            javaVersion = "21"
        )
    )
}
