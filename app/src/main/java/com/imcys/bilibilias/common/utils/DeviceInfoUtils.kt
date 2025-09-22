package com.imcys.bilibilias.common.utils

import android.content.Context
import android.os.Build

object DeviceInfoUtils {


    fun getMarketingName(): String? = getPropByBrand()

    private fun getPropByBrand(): String? {
        val brand = Build.BRAND?.lowercase() ?: return null
        val key = brandToPropKey[brand] ?: "ro.product.marketname"
        return getSystemProp(key)
    }

    private val brandToPropKey = mapOf(
        "xiaomi" to "ro.product.marketname",
        "redmi" to "ro.product.marketname",
        "huawei" to "ro.config.marketing_name",
        "honor" to "ro.config.marketing_name",
        "oppo" to "ro.oppo.market.name",
        "realme" to "ro.product.realme.marketname",
        "oneplus" to "ro.product.marketname",
        "vivo" to "ro.vivo.market.name",
        "iqoo" to "ro.product.marketname",
        "samsung" to "ro.product.model",   // 三星 model 即市场名
        "meizu" to "ro.product.marketname",
        "nubia" to "ro.product.marketname",
        "zte" to "ro.product.marketname",
        "asus" to "ro.product.marketname", // ROG
        "blackshark" to "ro.product.marketname",
        "smartisan" to "ro.product.marketname"
    )

    private fun getSystemProp(key: String): String? = try {
        val value = Class.forName("android.os.SystemProperties")
            .getDeclaredMethod("get", String::class.java)
            .invoke(null, key) as? String
        if (!value.isNullOrBlank()) value else null
    } catch (_: Throwable) {
        null
    }

    fun getDeviceInfoCopyString(context: Context): String {
        val deviceInfo = getDeviceInfo(context)

        return """
        APP版本：${deviceInfo.appVersion}
        系统版本：${deviceInfo.systemVersion}
        设备型号：${deviceInfo.model}
        市场型号：${deviceInfo.marketModel}
        厂商：${deviceInfo.manufacturer}
        品牌：${deviceInfo.brandName}
        厂商系统名称：${deviceInfo.osName}
        厂商系统版本名称：${deviceInfo.osVersionName}
    """.trimIndent()
    }

    fun getDeviceInfo(context: Context): DeviceInfo {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val packageInfo = try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
        val appVersion = packageInfo?.versionName ?: "未知"
        val systemVersion = Build.VERSION.RELEASE ?: "未知"
        val model = Build.MODEL ?: "未知"
        val marketModel = getMarketingName() ?: "未知"
        val manufacturer = Build.BRAND ?: "未知"
        val brand = Build.BRAND ?: "未知"
        val brandName = try {
            com.hjq.device.compat.DeviceBrand.getBrandName() ?: Build.DEVICE
        } catch (_: Throwable) {
            Build.DEVICE
        }
        val osName = try {
            com.hjq.device.compat.DeviceOs.getOsName() ?: "未知"
        } catch (_: Throwable) {
            "未知"
        }
        val osVersionName = try {
            com.hjq.device.compat.DeviceOs.getOsVersionName() ?: "未知"
        } catch (_: Throwable) {
            "未知"
        }
        return DeviceInfo(
            appVersion = appVersion,
            systemVersion = systemVersion,
            model = model,
            marketModel = marketModel,
            manufacturer = manufacturer,
            brand = brand,
            brandName = brandName,
            osName = osName,
            osVersionName = osVersionName
        )
    }

}

/**
 * 设备信息
 */
data class DeviceInfo(
    val appVersion: String,
    val systemVersion: String,
    val model: String,
    val marketModel: String,
    val manufacturer: String,
    val brand: String,
    val brandName: String,
    val osName: String,
    val osVersionName: String
)

