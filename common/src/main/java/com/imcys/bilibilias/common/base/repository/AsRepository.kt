package com.imcys.bilibilias.common.base.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.model.OldHomeBannerDataBean
import com.imcys.bilibilias.common.base.model.OldUpdateDataBean
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import javax.inject.Inject
import kotlin.system.exitProcess

class AsRepository @Inject constructor(private val httpClient: HttpClient) {
    suspend fun getBannerData() {
        httpClient.get(BiliBiliAsApi.updateDataPath) {
            parameter("type", "banner")
        }.body<OldHomeBannerDataBean>()
    }

    suspend fun getSignature() {
        val body = httpClient.get(BiliBiliAsApi.updateDataPath) {
            parameter("type", "json")
            parameter("version", BiliBiliAsApi.version)
        }.body<OldUpdateDataBean>()
        body.apkMD5
        body.apkToKenCR
    }

    /**
     * 送出此版本的数据信息
     */
    suspend fun uploadSignature(context: Context): Triple<String?, String?, String?> {
        val sha = apkVerifyWithSHA(context)
        val md5 = apkVerifyWithMD5(context)
        val crc = apkVerifyWithCRC(context)
        val LJ = apkLength(context)
        httpClient.get(BiliBiliAsApi.updateDataPath) {
            parameter("SHA", sha)
            parameter("MD5", md5)
            parameter("CRC", crc)
            parameter("lj", LJ)
        }
        return Triple(sha, md5, crc)
    }

    /**
     * 核对APP数据
     */
    // todo 检查 app 数据
    private fun checkAppData(it: OldUpdateDataBean, sha: String?, md5: String?, crc: String?) {
        if (sha == null || md5 == null || crc == null) {
            val uri: Uri = Uri.parse(it.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(TODO(), TODO(), null)
            exitProcess(0)
        }
    }

    private fun apkLength(context: Context): String? {
        return calculateSignature {
            val apkPath = context.packageCodePath
            apkPath.length.toString()
        }
    }
    // 底层程序加固 -> 防止程序被修改从多个角度检测安装包完整性
    /**
     * 通过检查签名文件classes.dex文件的哈希值来判断代码文件是否被篡改
     */
    private fun apkVerifyWithSHA(context: Context): String? {
        return calculateSignature {
            val apkPath = context.packageCodePath // 获取Apk包存储路径
            val dexDigest: MessageDigest = MessageDigest.getInstance("SHA-1")
            val bytes = ByteArray(1024)
            var byteCount: Int
            val fis = FileInputStream(File(apkPath)) // 读取apk文件
            while (fis.read(bytes).also { byteCount = it } != -1) {
                dexDigest.update(bytes, 0, byteCount)
            }
            val bigInteger = BigInteger(1, dexDigest.digest()) // 计算apk文件的哈希值
            val sha: String = bigInteger.toString(16)
            fis.close()
            sha
        }
    }

    /**
     * 通过检查apk包的MD5摘要值来判断代码文件是否被篡改
     */
    private fun apkVerifyWithMD5(context: Context): String? {
        return calculateSignature {
            val apkPath = context.packageCodePath // 获取Apk包存储路径
            val dexDigest: MessageDigest = MessageDigest.getInstance("MD5")
            val bytes = ByteArray(1024)
            var byteCount: Int
            val fis = FileInputStream(File(apkPath)) // 读取apk文件
            while (fis.read(bytes).also { byteCount = it } != -1) {
                dexDigest.update(bytes, 0, byteCount)
            }
            val bigInteger = BigInteger(1, dexDigest.digest()) // 计算apk文件的哈希值
            val sha: String = bigInteger.toString(16)
            fis.close()
            sha
        }
    }

    /**
     * 通过检查classes.dex文件的CRC32摘要值来判断文件是否被篡改
     */
    private fun apkVerifyWithCRC(context: Context): String? {
        return calculateSignature {
            val apkPath = context.packageCodePath // 获取Apk包存储路径
            val zipFile = ZipFile(apkPath)
            val dexEntry: ZipEntry = zipFile.getEntry("classes.dex") // 读取ZIP包中的classes.dex文件
            dexEntry.crc.toString()
        }
    }

    private fun calculateSignature(block: () -> String): String? =
        try {
            block()
        } catch (e: FileNotFoundException) {
            Timber.tag("apkVerify").i(e)
            null
        } catch (e: NoSuchAlgorithmException) {
            Timber.tag("apkVerify").i(e)
            null
        } catch (e: IOException) {
            Timber.tag("apkVerify").i(e)
            null
        }
}
