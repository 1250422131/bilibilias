package com.bilias.crash.policy

import android.os.Environment
import android.util.Log
import com.bilias.crash.Info
import com.bilias.crash.group.DefaultGroup
import com.bilias.crash.group.Group
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 把崩溃日志信息保存到SD卡，需要申请存储权限
 *
 * [folderName]手机系统根目录保存日志文件夹的名称
 */
class StoragePolicy(
    private val folderName: String = "android-bilias",
    policy: CrashReportPolicy? = null,
    override val group: Group = DefaultGroup()
) : CrashReportPolicy(group, policy) {
    override fun report(info: Info, group: Group) {
        super.report(info, group)
        try {
            if (group.counts()) {
                val path = Environment.getExternalStorageDirectory().absolutePath
                val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA)
                val time = simpleDateFormat.format(Date())
                val folder = File(path, folderName)
                folder.mkdirs()
                val file = File(folder.absolutePath, "crash$time.txt")
                if (!file.exists()) {
                    file.createNewFile()
                }
                val buffer = info.toString().toByteArray()
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.write(buffer, 0, buffer.size)
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        } catch (e: IOException) {
            Log.e(this.javaClass.simpleName, e.toString())
        }
    }
}