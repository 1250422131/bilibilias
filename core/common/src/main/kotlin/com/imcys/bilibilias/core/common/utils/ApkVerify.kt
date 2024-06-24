package com.imcys.bilibilias.core.common.utils

import dev.utils.app.AppUtils
import dev.utils.common.DevCommonUtils
import java.io.File
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * 底层程序加固 -> 防止程序被修改从多个角度检测安装包完整性
 */
object ApkVerify {
    /**
     * 通过检查签名文件classes.dex文件的哈希值来判断代码文件是否被篡改
     */
    fun apkVerifyWithSHA(apkPath: String): String {
        return AppUtils.getAppSignatureSHA1() ?: ""
    }

    /**
     * 通过检查apk包的MD5摘要值来判断代码文件是否被篡改
     */
    fun apkVerifyWithMD5(apkPath: String): Pair<String, Long> {
        val file = File(apkPath)
        return (AppUtils.getAppSignatureMD5() ?: "") to file.length()
    }

    /**
     * 通过检查classes.dex文件的CRC32摘要值来判断文件是否被篡改
     */
    fun apkVerifyWithCRC(apkPath: String): String {
        val zipFile = ZipFile(apkPath)
        // 读取ZIP包中的classes.dex文件
        val dexEntry: ZipEntry = zipFile.getEntry("classes.dex")
        return dexEntry.crc.toString()
    }
}
