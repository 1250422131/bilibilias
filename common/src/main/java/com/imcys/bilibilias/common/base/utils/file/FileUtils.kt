package com.imcys.bilibilias.common.base.utils.file

import com.microsoft.appcenter.utils.storage.FileManager.deleteDirectory
import timber.log.Timber
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * 文件操作类
 */
object FileUtils {
    private const val TAG = "FileUtils"

    /**
     * 检查是否存在某个文件
     * @param file 文件
     * @return `true` yes, `false` no
     */
    fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    /**
     * 检查是否存在某个文件
     * @param filePath 文件路径
     * @return `true` yes, `false` no
     */
    fun isFileExists(filePath: String?): Boolean {
        return isFileExists(getFileByPath(filePath))
    }

    /**
     * 检查是否存在某个文件
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return `true` yes, `false` no
     */
    fun isFileExists(filePath: String?, fileName: String?): Boolean {
        return filePath != null && fileName != null && File(filePath, fileName).exists()
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @return `true` success, `false` fail
     */
    fun deleteFile(filePath: String?): Boolean {
        return deleteFile(getFileByPath(filePath))
    }

    /**
     * 获取文件
     * @param filePath 文件路径
     * @return 文件 [File]
     */
    fun getFileByPath(filePath: String?): File? {
        return if (filePath != null) File(filePath) else null
    }

    /**
     * 删除文件
     * @param file 文件
     * @return `true` success, `false` fail
     */
    fun deleteFile(file: File?): Boolean {
        // 文件存在, 并且不是目录文件, 则直接删除
        return if (file != null && file.exists() && !file.isDirectory) {
            file.delete()
        } else {
            false
        }
    }

    /**
     * 删除多个文件
     * @param filePaths 文件路径数组
     * @return `true` success, `false` fail
     */
    fun deleteFiles(vararg filePaths: String?): Boolean {
        if (filePaths.isNotEmpty()) {
            for (filePath in filePaths) {
                deleteFile(filePath)
            }
            return true
        }
        return false
    }

    /** 删除文件，可以是文件或文件夹
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    fun delete(delFile: String): Boolean {
        val file = File(delFile)
        return if (!file.exists()) {
            false
        } else {
            if (file.isFile) mDeleteFile(delFile) else deleteDirectory(File(delFile))
        }
    }

    /**
     * 删除单个文件
     * @param sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private fun mDeleteFile(sPath: String?): Boolean {
        var flag = false
        val file = File(sPath!!)
        // 路径为文件且不为空则进行删除
        if (file.isFile && file.exists()) {
            file.delete()
            flag = true
        }
        return flag
    }

    /**
     * 读取文件
     * <pre>
     * 获取换行内容可以通过
     * </pre>
     * @param filePath 文件路径
     * @return 文件内容字符串
     */
    fun readFile(filePath: String?): String? {
        return readFile(getFileByPath(filePath))
    }

    /**
     * 读取文件
     * @param file 文件
     * @return 文件内容字符串
     */
    fun readFile(file: File?): String? {
        if (file != null && file.exists()) {
            try {
                return readFile(FileInputStream(file))
            } catch (e: Exception) {
                Timber.tag(TAG).d(e, "readFile")
            }
        }
        return null
    }

    /**
     * 读取文件
     * @param inputStream [InputStream] new FileInputStream(path)
     * @return 文件内容字符串
     */
    fun readFile(inputStream: InputStream?): String? {
        return readFile(inputStream, null)
    }

    /**
     * 读取文件
     * @param inputStream [InputStream] new FileInputStream(path)
     * @param encode      编码格式
     * @return 文件内容字符串
     */
    fun readFile(
        inputStream: InputStream?,
        encode: String?
    ): String? {
        if (inputStream != null) {
            var br: BufferedReader? = null
            try {
                val isr: InputStreamReader = if (encode != null) {
                    InputStreamReader(inputStream, encode)
                } else {
                    InputStreamReader(inputStream)
                }
                br = BufferedReader(isr)
                val builder = StringBuilder()
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                return builder.toString()
            } catch (e: java.lang.Exception) {
                Timber.tag(TAG).d(e, "readFile")
            } finally {
                br?.close()
            }
        }
        return null
    }

    fun fileWrite(path: String?, Str: String?) {
        val file = File(path.toString())
        if (!file.exists()) {
            file.parentFile?.mkdirs()
        }
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val fw = FileWriter(file)
        val bw = BufferedWriter(fw)
        bw.write(Str)
        bw.flush()
        bw.close()
        fw.close()
    }
}
