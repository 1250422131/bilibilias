import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileOutputStream
import java.net.URI

class BaiduJarDownloadConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 只在 app module 下生效
        if (target.name != "app") return

        val libsDir = File(target.projectDir, "libs")
        val downloadUrl = "https://drive.misakamoe.com/d/TB_drive/BILIBILI_AS/BaiduSDK/Baidu_Mtj_android_4.0.11.0.jar"
        val expectedJarName = downloadUrl.substringAfterLast('/')

        // --- 自动检查和处理 jar 文件 ---
        if (!libsDir.exists()) libsDir.mkdirs()
        val baiduJars = libsDir.listFiles { file ->
            file.isFile && file.name.contains("Baidu_Mtj_android") && file.name.endsWith(".jar")
        } ?: emptyArray()
        val matched = baiduJars.any { it.name == expectedJarName }
        if (matched) {
            println("$expectedJarName already exists, no action needed.")
        } else {
            // 删除所有旧的 Baidu_Mtj_android jar
            baiduJars.forEach { oldJar ->
                println("Deleting old jar: ${oldJar.name}")
                oldJar.delete()
            }
            // 下载新 jar
            val newJarFile = File(libsDir, expectedJarName)
            println("Downloading $expectedJarName from $downloadUrl ...")
            try {
                val url = URI(downloadUrl).toURL()
                url.openConnection().apply {
                    connect()
                    getInputStream().use { input ->
                        FileOutputStream(newJarFile).use { output ->
                            val buffer = ByteArray(8192)
                            var bytesRead: Int
                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                            }
                        }
                    }
                }
                println("$expectedJarName downloaded to ${newJarFile.absolutePath}")
            } catch (e: Exception) {
                println($$"Failed to download $$expectedJarName: $${e.message}")
            }
        }

        // 保留 task 以便手动触发
        target.tasks.register("ensureBaiduJar") {
            group = "verification"
            description = "Ensure correct Baidu_Mtj_android jar exists in app/libs"
            doLast {}
        }
    }
}
