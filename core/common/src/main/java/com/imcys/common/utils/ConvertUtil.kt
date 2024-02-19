package com.imcys.common.utils

object ConvertUtil {
    private const val TABLE = "FcwAPNKTMug3GV5Lj7EJnHpWsx4tb8haYeviqBz6rkCy12mUSDQX9RdoZf"
    private const val XOR = 23442827791579L
    private const val MASK = 2251799813685247L
    private const val MAX_AID = 1L shl 51
    private val Tr = mutableMapOf<Char, Int>()

    init {
        for (i in 0..57) {
            Tr[TABLE[i]] = i
        }
    }

    fun Bv2Av(bvId: String): Long {
        val bv = bvId.toCharArray()
        swap(bv)
        var tmp = 0L
        for (bvIdx in 3 until bv.size) {
            val tableIdx = Tr[bv[bvIdx]]!!
            tmp = tmp * TABLE.length + tableIdx.toLong()
        }
        return tmp and MASK xor XOR
    }

    fun Av2Bv(avId: Long): String {
        val bv = charArrayOf('B', 'V', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0')
        var bvIdx = 11
        var tmp = MAX_AID or avId xor XOR
        while (tmp != 0L) {
            val tableIdx = (tmp % TABLE.length).toInt()
            val cc = TABLE[tableIdx]
            bv[bvIdx] = cc
            tmp /= TABLE.length
            bvIdx--
        }
        swap(bv)
        return String(bv)
    }

    private fun swap(chars: CharArray) {
        swap(chars, 3, 9)
        swap(chars, 4, 7)
    }

    private fun swap(a: CharArray, i: Int, j: Int) {
        val t = a[i]
        a[i] = a[j]
        a[j] = t
    }
}