package com.imcys.bilibilias.common.base.utils

// 新的BV转换
// https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/misc/bvid_desc.md
object NewVideoNumConversionUtils {
    private const val XOR_CODE = 23442827791579
    private const val MASK_CODE = 2251799813685247
    private const val MAX_AID = 1L shl 51
    private const val ALPHABET = "FcwAPNKTMug3GV5Lj7EJnHpWsx4tb8haYeviqBz6rkCy12mUSDQX9RdoZf"
    private val ENCODE_MAP = listOf(8, 7, 0, 5, 1, 3, 2, 4, 6)
    private val DECODE_MAP = ENCODE_MAP.reversed()

    private const val BASE = ALPHABET.length
    private const val PREFIX = "BV1"
    private val CODE_LEN = ENCODE_MAP.size

    fun av2bv(aid: Long): String {
        val bvid = CharArray(9)
        var tmp = (MAX_AID or aid) xor XOR_CODE
        for (i in 0 until CODE_LEN) {
            bvid[ENCODE_MAP[i]] = ALPHABET[(tmp % BASE).toInt()]
            tmp /= BASE
        }
        return PREFIX + bvid.joinToString("")
    }

    fun bv2av(bvid: String): Long {
        require(bvid.substring(0, 3) == PREFIX)

        val mBvid = bvid.substring(3)
        var tmp = 0.toBigInteger()
        for (i in 0 until CODE_LEN) {
            val idx = ALPHABET.indexOf(mBvid[DECODE_MAP[i]])
            tmp = tmp * BASE.toBigInteger() + idx.toBigInteger()
        }

        return ((tmp and MASK_CODE.toBigInteger()) xor XOR_CODE.toBigInteger()).toLong()
    }



}