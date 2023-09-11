package com.imcys.bilibilias.common.base.utils

import kotlin.math.pow

/**
 * [thanks mcfx](https://www.zhihu.com/question/381784377/answer/1099438784)
 * [av转bv类](https://github.com/BakaJzon/bv2av-java/blob/master/src/bakajzon/bv2av/Offline.java)
 *
 * @author BakaJzon
 */
object VideoUtils {

    @Deprecated(
        "",
        ReplaceWith("use av2bv", "com.imcys.bilibilias.common.base.utils.VideoUtils.av2bv")
    )
    fun toBvidOffline(avid: Long): String {
        return av2Bv(avid)
    }

    private const val TABLE = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF"
    private val S = intArrayOf(11, 10, 3, 8, 4, 6)
    private const val XOR = 177451812L
    private const val ADD = 8728348608L
    private val MAP: MutableMap<Char, Int> = mutableMapOf()

    init {
        for (i in 0..57) {
            MAP[TABLE[i]] = i
        }
    }

    fun av2Bv(aid: Long): String {
        val x = (aid xor XOR) + ADD
        val chars = charArrayOf('B', 'V', '1', ' ', ' ', '4', ' ', '1', ' ', '7', ' ', ' ')
        for (i in 0..5) {
            val pow = 58.0.pow(i.toDouble()).toInt()
            val i1 = x / pow
            val index = (i1 % 58).toInt()
            chars[S[i]] = TABLE[index]
        }
        return String(chars)
    }

    fun bv2Av(bvid: String): Int {
        var r: Long = 0
        for (i in 0..5) {
            r += (MAP[bvid[S[i]]]!! * 58.0.pow(i.toDouble())).toLong()
        }
        return (r - ADD xor XOR).toInt()
    }
}
