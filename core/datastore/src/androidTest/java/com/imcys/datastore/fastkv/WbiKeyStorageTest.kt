package com.imcys.datastore.fastkv

import androidx.test.core.app.*
import io.mockk.*
import kotlinx.datetime.*
import org.junit.*
import kotlin.test.*

class WbiKeyStorageTest {
    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private val storage = WbiKeyStorage(ApplicationProvider.getApplicationContext())

    @Before
    fun resetDate() {
        setDate(null)
    }

    @Test
    fun updateLocalDate() {
        val date = InternalPlatformDsl.dynamicGet(storage, "recordLocalDate") as String?
        assertEquals(null, date)
        storage.updateLocalDate()
        assertEquals(InternalPlatformDsl.dynamicCall(storage, "today", arrayOf(), mockk()), date)
    }

    @Test
    fun testShouldUpdate() {
        setDate("")
        assertEquals(true, storage.shouldUpdate())

        setDate("2024-02-07")
        assertEquals(true, storage.shouldUpdate())

        setDate(today.toString())
        assertEquals(false, storage.shouldUpdate())
    }

    private fun setDate(date: String?) {
        InternalPlatformDsl.dynamicSet(storage, "recordLocalDate", date)
    }
}