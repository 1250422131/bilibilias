package com.imcys.bilibilias.core.datasource.utils

import io.ktor.http.encodeURLParameter
import org.jetbrains.annotations.TestOnly
import java.security.MessageDigest

internal object WbiSign {
    private val mixinKeyEncTab = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
        33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
        61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
        36, 20, 34, 44, 52
    )

    @TestOnly
    internal lateinit var mixinKey: String
        private set

    fun initializeMixinKey(imgKey: String, subKey: String) {
        val imgKeyName = imgKey.substringAfterLast('/', "").removeSuffix(".png")
        val subKeyName = subKey.substringAfterLast('/', "").removeSuffix(".png")
        val combinedSource = imgKeyName + subKeyName
        if (combinedSource.length < 32) {
            throw IllegalArgumentException("Combined key source is too short to generate mixinKey. Ensure imgKey and subKey are valid.")
        }
        mixinKey = buildString {
            mixinKeyEncTab.take(32)
                .forEach { index -> // Iterate only over the first 32 elements of mixinKeyEncTab
                    if (index >= combinedSource.length) {
                        throw IllegalArgumentException("Invalid index in mixinKeyEncTab for the given combinedKeySource length.")
                    }
                    append(combinedSource[index])
                }
        }
    }

    fun enc(params: Map<String, Any>): String {
        if (!::mixinKey.isInitialized) {
            throw IllegalStateException("mixinKey has not been initialized. Call initializeMixinKey(String, String) first.")
        }
        val sortedMap = params.toSortedMap()
        val currentTimeSeconds = System.currentTimeMillis() / 1000
        if (!sortedMap.containsKey("wts")) {
            sortedMap["wts"] = currentTimeSeconds
        }
        val originalQueryString = sortedMap.entries.joinToString("&") { (k, v) ->
            "${k.encodeURLParameter()}=${v.toString().encodeURLParameter()}"
        }
        val wRid = (originalQueryString + mixinKey).toMD5()

        return buildString {
            append(originalQueryString)
            append("&w_rid=")
            append(wRid)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray())
        return digest.toHexString()
    }
}