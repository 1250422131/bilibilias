package com.imcys.bilibilias.common.base.utils.file

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.microsoft.appcenter.utils.storage.FileManager.deleteDirectory
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * 文件操作类
 */
object FileUtils {

    fun isFileExists(file: File): Boolean {
        return file.exists() && !file.isDirectory
    }

    /**
     * 删除文件
     * @param sPath String
     * @return Boolean
     */
    fun deleteFile(sPath: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val path = Paths.get(sPath)
            Files.deleteIfExists(path)
        } else {
            mDeleteFile(sPath)
        }
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
            flag = file.delete()
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

    fun getRealPathFromURI(contentUri: Uri, context: Context): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}
