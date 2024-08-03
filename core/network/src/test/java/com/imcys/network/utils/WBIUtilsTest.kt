package com.imcys.network.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class WBIUtilsTest {
    @Test
    fun signature() {
        val imgKey = "653657f524a547ac981ded72ea172057"
        val subKey = "6e4909c702f846728e64f6007736a338"
        val mixKey = WBIUtils.getMixinKey(imgKey, subKey)
        assertEquals("72136226c6a73669787ee4fd02a74c27", mixKey)
    }
}
