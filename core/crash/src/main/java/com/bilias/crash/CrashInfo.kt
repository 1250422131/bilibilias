package com.bilias.crash

/**
 * [thread] 崩溃线程
 *
 * [throwable] 崩溃异常信息
 */
data class CrashInfo(val thread: Thread, val throwable: Throwable, val appInfo: AppInfo) : Info {
    private val exception: String
        get() = buildStackTrace(throwable.stackTrace)

    private fun buildStackTrace(lines: Array<StackTraceElement>): String {
        val sb = StringBuilder()
        for (line in lines) {
            sb.append("\n")
                .append("at ")
                .append(line.className).append(".").append(line.methodName)
                .append("(").append(line.fileName + ":" + line.lineNumber).append(")")
        }
        return sb.toString()
    }

    override fun toString(): String {
        return """
             Crash Thread:${thread.name}#${thread.id}
             Mobile Model:${appInfo.model}
             Mobile Brand:${appInfo.brand}
             SDK Version:${appInfo.sdkVersion}
             Android Version:${appInfo.release}
             Version Name:${appInfo.pkgName}
             Version Code:${appInfo.versionCode}
             Exception Information:$throwable${exception}
             """.trimIndent()
    }
}
