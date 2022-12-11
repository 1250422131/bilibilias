package com.imcys.bilibilias.utils

import java.io.*

/**
 * 文件操作类
 */
object FileUtils {


    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    fun deleteFile(sPath: String?): Boolean {
        var flag = false
        val file = File(sPath!!)
        // 路径为文件且不为空则进行删除
        if (file.isFile && file.exists()) {
            file.delete()
            flag = true
        }
        return flag
    }


    fun fileRead(path: String?): String? {
        val file = File(path.toString())
        if (!file.exists()) {
            file.parentFile?.mkdirs()
        }
        file.createNewFile()

        // read
        val fr = FileReader(file)
        val br = BufferedReader(fr)
        return br.readLine()
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