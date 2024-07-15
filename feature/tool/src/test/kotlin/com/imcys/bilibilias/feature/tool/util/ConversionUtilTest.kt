package com.imcys.bilibilias.feature.tool.util

import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.hasLength
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.startsWith
import kotlin.test.Test
import kotlin.test.assertEquals

class ConversionUtilTest {
    @Test
    fun av2bv() {
        val bv = ConversionUtil.av2bv(111298867365120)
        expectThat(bv) {
            hasLength(12)
            startsWith("BV1")
            isEqualTo("BV1L9Uoa9EUx")
        }
    }

    @Test
    fun av2bv_avIsSmall() {
        expectCatching { ConversionUtil.av2bv(-1) }
            .isFailure()
    }

    @Test
    fun av2bv_avIsBig() {
        expectCatching { ConversionUtil.av2bv(1L shl 52) }
            .isFailure()
    }

    @Test
    fun bv2av() {
        val av = ConversionUtil.bv2av("BV1L9Uoa9EUx")
        assertEquals(111298867365120, av)
    }
}
