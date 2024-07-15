package com.imcys.bilibilias.feature.tool.util

object ConversionUtil {

    private const val XOR_CODE = 23442827791579
    private const val MASK_CODE = 2251799813685247

    private const val MAX_AID = 1L shl 51
    private const val MIN_AID = 1L

    private const val BASE = 58
    private const val BV_LEN = 12
    private const val PREFIX = "BV1"
    private const val DATA = "FcwAPNKTMug3GV5Lj7EJnHpWsx4tb8haYeviqBz6rkCy12mUSDQX9RdoZf"

    fun av2bv(avid: Long): String {
        check(avid > MIN_AID) { "Av $avid is smaller than $MIN_AID" }
        check(avid < MAX_AID) { "Av $avid is bigger than $MAX_AID" }

        var tmp = MAX_AID or avid xor XOR_CODE
        val bytes = charArrayOf('0', '0', '0', '0', '0', '0', '0', '0', '0')
        var index = bytes.lastIndex
        while (tmp > 0) {
            bytes[index] = DATA[tmp.mod(BASE)]
            tmp /= BASE
            index--
        }
        swap(bytes, 0, 6)
        swap(bytes, 1, 4)
        return PREFIX + bytes.joinToString("")
    }

    fun bv2av(bv: String): Long {
        check(bv.length == BV_LEN) { "Bv is empty" }
        val array = bv.drop(3).toCharArray()
        swap(array, 0, 6)
        swap(array, 1, 4)
        var tmp = 0L
        for (c in array) {
            tmp = tmp * BASE + DATA.indexOf(c)
        }
        return tmp and MASK_CODE xor XOR_CODE
    }

    private fun swap(array: CharArray, i: Int, j: Int) {
        val temp = array[i]
        array[i] = array[j]
        array[j] = temp
    }
}
