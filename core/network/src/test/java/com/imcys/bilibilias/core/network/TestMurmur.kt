package com.imcys.bilibilias.core.network

import io.ktor.utils.io.core.toByteArray
import okhttp3.internal.toHexString
import org.junit.Test
import kotlin.test.assertEquals
import com.goncalossilva.murmurhash.MurmurHash3 as GoncalossilvaMurmurHash3
import org.apache.commons.codec.digest.MurmurHash3 as ApachMurmurHash3

class TestMurmur {

    @Test
    fun `test murmurhash1`() {
        val bytes = FP.toByteArray()
        val result = ApachMurmurHash3.hash128x64(bytes, 0, bytes.size, 31)

        // -2298316791673655739, 8062900387719939003
        // e01abd0e12f9ee45 6fe52d2efd6803bb
        println(result.joinToString())
        val r1 = result[0].toHexString()
        val r2 = result[1].toHexString()

        assertEquals(r1, "e01abd0e12f9ee45")
        assertEquals(r2, "6fe52d2efd6803bb")
        assertEquals(r1 + r2, "e01abd0e12f9ee456fe52d2efd6803bb")
    }

    @Test
    fun `test murmurhash2`() {
        com.goncalossilva.murmurhash.MurmurHash3()
        val bytes = "".toByteArray()
        val result = ApachMurmurHash3.hash128x64(bytes, 0, bytes.size, 31)

        println(result.joinToString())
        val r1 = result[0].toHexString()
        val r2 = result[1].toHexString()

        assertEquals(r1, "24700f9f1986800a")
        assertEquals(r2, "b4fcc880530dd0ed")
        assertEquals(r1 + r2, "24700f9f1986800ab4fcc880530dd0ed")
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test murmurhash3`() {
        val hash3 = GoncalossilvaMurmurHash3(31u)
        val bytes = FP.toByteArray()
        val result = hash3.hash128x64(bytes)

        println(result.joinToString())
        val r1 = result[0].toHexString()
        val r2 = result[1].toHexString()

        assertEquals(r1, "e01abd0e12f9ee45")
        assertEquals(r2, "6fe52d2efd6803bb")
        assertEquals(r1 + r2, "e01abd0e12f9ee456fe52d2efd6803bb")
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test murmurhash4`() {
        val hash3 = GoncalossilvaMurmurHash3(31u)
        val bytes = "".toByteArray()

        val result = hash3.hash128x64(bytes)
        println(result.joinToString())
        println(result.joinToString())

        val r1 = result[0].toHexString()
        val r2 = result[1].toHexString()

        assertEquals(r1, "24700f9f1986800a")
        assertEquals(r2, "b4fcc880530dd0ed")
        val r3 = result.map(ULong::toHexString).joinToString("")
        assertEquals(r3, "24700f9f1986800ab4fcc880530dd0ed")
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test murmurhash5`() {
        val r3 = buvidFp(payload(context, json))
        assertEquals(r3, "e01abd0e12f9ee456fe52d2efd6803bb")
    }
}
