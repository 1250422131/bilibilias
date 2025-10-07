import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import java.io.File


class FFmpegVerificationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val enablePlayAppMode: String by target
        with(target) {
            // 配置常量
            val ffmpegDir = file("src/main/cpp/ffmpeg")
            val ffmpegVersionFile = File(ffmpegDir, "as-ffmpeg-version")
            val releaseUrl = "https://github.com/1250422131/bilibilias-ffmpeg/releases"
            val ffmpegExpectedVersion = findProperty("as.ffmpeg.version")?.toString()?.trim()

            // 创建警告任务（同步时）
            tasks.register("warnFFmpegMissing") {
                description = "Warn if FFmpeg libraries are missing or version mismatch during sync"
                group = "verification"

                doFirst {
                    if (!isFFmpegAvailable(ffmpegDir, ffmpegVersionFile)) {
                        printWarning(releaseUrl)
                    } else if (!ffmpegExpectedVersion.isNullOrEmpty()) {
                        val actualVersion = ffmpegVersionFile.readText().trim()
                        if (actualVersion != ffmpegExpectedVersion) {
                            printVersionMismatchWarning(releaseUrl, ffmpegExpectedVersion, actualVersion)
                        }
                    }
                }
            }

            // 创建强制验证任务（编译前）
            tasks.register("requireFFmpeg") {
                description = "Ensure FFmpeg libraries exist before build"
                group = "verification"

                doFirst {
                    if (!isFFmpegAvailable(ffmpegDir, ffmpegVersionFile)) {
                        throw GradleException(buildErrorMessage(releaseUrl, ffmpegExpectedVersion, null))
                    } else {
                        val actualVersion = ffmpegVersionFile.readText().trim()
                        if (ffmpegExpectedVersion != null && actualVersion != ffmpegExpectedVersion) {
                            throw GradleException(buildErrorMessage(releaseUrl, ffmpegExpectedVersion, actualVersion))
                        }
                        logger.lifecycle("✅ FFmpeg libraries found (version: $actualVersion)")
                    }
                }
            }

            // 在 preBuild 任务前添加验证
            tasks.matching { it.name == "preBuild" }.configureEach {
                dependsOn("requireFFmpeg")
            }

            // 项目评估后检查（同步时警告）
            afterEvaluate {
                val ffmpegExists = isFFmpegAvailable(ffmpegDir, ffmpegVersionFile)
                if (!ffmpegExists) {
                    printWarning(releaseUrl)
                } else if (!ffmpegExpectedVersion.isNullOrEmpty()) {
                    val actualVersion = ffmpegVersionFile.readText().trim()
                    if (actualVersion != ffmpegExpectedVersion) {
                        printVersionMismatchWarning(releaseUrl, ffmpegExpectedVersion, actualVersion)
                    }
                }
            }
        }
    }

    private fun isFFmpegAvailable(ffmpegDir: File, versionFile: File): Boolean {
        return ffmpegDir.exists() && versionFile.exists()
    }

    private fun printWarning(releaseUrl: String) {
        val separator = "=".repeat(75)
        println()
        println(separator)
        System.err.println("⚠️  WARNING: FFmpeg libraries not found!")
        println(separator)
        System.err.println("Missing: core/ffmpeg/src/main/cpp/ffmpeg")
        System.err.println("")
        System.err.println("📥 Download from: $releaseUrl")
        println(separator)
        println()
    }

    private fun printVersionMismatchWarning(releaseUrl: String, expectedVersion: String, actualVersion: String) {
        val separator = "=".repeat(75)
        println()
        println(separator)
        System.err.println("⚠️  WARNING: FFmpeg version mismatch!")
        println(separator)
        System.err.println("Expected version: $expectedVersion")
        System.err.println("Actual version:   $actualVersion")
        System.err.println("")
        System.err.println("请下载最新 FFmpeg: $releaseUrl")
        println(separator)
        println()
    }

    private fun buildErrorMessage(releaseUrl: String, expectedVersion: String?, actualVersion: String?): String {
        return buildString {
            appendLine()
            appendLine("❌  BUILD FAILED: FFmpeg libraries required but not found!")
            appendLine()
            appendLine("Missing directory: core/ffmpeg/src/main/cpp/ffmpeg")
            appendLine()
            appendLine("📥 Please download FFmpeg:")
            appendLine(releaseUrl)
            appendLine()
            appendLine("📂 Extract to: core/ffmpeg/src/main/cpp/")
            appendLine()
            appendLine("Expected structure:")
            appendLine("  ffmpeg/")
            appendLine("    ├── arm64-v8a/lib/")
            appendLine("    ├── armeabi-v7a/lib/")
            appendLine("    ├── x86_64/lib/")
            appendLine("    ├── include/")
            appendLine("    └── as-ffmpeg-version")
            appendLine()

            if (expectedVersion != null) {
                appendLine("Expected version: $expectedVersion")
            }

            if (actualVersion != null) {
                appendLine("Actual version: $actualVersion")
            }
        }
    }
}