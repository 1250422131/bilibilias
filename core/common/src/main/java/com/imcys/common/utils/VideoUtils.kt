package com.imcys.common.utils

import java.math.BigInteger

/**
 * [thanks mcfx](https://www.zhihu.com/question/381784377/answer/1099438784)
 * [av转bv类](https://github.com/BakaJzon/bv2av-java/blob/master/src/bakajzon/bv2av/Offline.java)
 *
 * @author BakaJzon
 */
@Deprecated("新工具", ReplaceWith("com.imcys.common.utils.ConvertUtil"))
object VideoUtils {

    @Deprecated(
        "",
        ReplaceWith(
            "VideoUtils.av2Bv(aid)",
            "com.imcys.bilibilias.common.base.utils.VideoUtils.av2bv"
        )
    )
    fun toBvidOffline(avid: Long): String {
        return av2bv(avid)
    }

    private val XOR_CODE = BigInteger("23442827791579")
    private val MASK_CODE = BigInteger("2251799813685247")
    private val MAX_AID = BigInteger.ONE.shiftLeft(51)
    private val BASE = BigInteger("58")

    private const val data = "FcwAPNKTMug3GV5Lj7EJnHpWsx4tb8haYeviqBz6rkCy12mUSDQX9RdoZf"
    fun av2bv(aid: Long): String {
        val av = BigInteger(aid.toString())
        val bytes = mutableListOf('B', 'V', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0')
        var bvIndex = bytes.size - 1
        var tmp = (MAX_AID.or(av)) xor XOR_CODE
        while (tmp > BigInteger.ZERO) {
            bytes[bvIndex] = data[tmp.mod(BASE).toInt()]
            tmp = tmp.divide(BASE)
            bvIndex -= 1
        }
        bytes[3] = bytes[9].also { bytes[9] = bytes[3] }
        bytes[4] = bytes[7].also { bytes[7] = bytes[4] }
        return bytes.joinToString("")
    }

    fun bv2av(bvid: String): Long {
        val bvidArr = bvid.toMutableList()
        bvidArr[3] = bvidArr[9].also { bvidArr[9] = bvidArr[3] }
        bvidArr[4] = bvidArr[7].also { bvidArr[7] = bvidArr[4] }
        bvidArr.removeAt(0)
        bvidArr.removeAt(0)
        bvidArr.removeAt(0)
        val tmp = bvidArr.fold(BigInteger.ZERO) { pre, bvidChar ->
            pre.multiply(BASE).add(BigInteger.valueOf(data.indexOf(bvidChar).toLong()))
        }
        return ((tmp.and(MASK_CODE)) xor XOR_CODE).toLong()
    }
}
