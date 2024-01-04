package com.imcys.common.utils

import org.junit.Test
import kotlin.test.assertEquals

class ConvertUtilTest {
    @Test
    fun bv2Av() {
        val bv = ConvertUtil.Av2Bv(111298867365120)
        assertEquals("BV1L9Uoa9EUx", bv)
    }

    @Test
    fun av2Bv() {
        val av = ConvertUtil.Bv2Av("BV1L9Uoa9EUx")
        assertEquals(111298867365120, av)
    }
}
