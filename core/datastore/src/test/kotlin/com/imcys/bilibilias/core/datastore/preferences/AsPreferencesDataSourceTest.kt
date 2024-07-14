package com.imcys.bilibilias.core.datastore.preferences

import androidx.datastore.core.CorruptionException
import com.imcys.bilibilias.core.datastore.UserPreferences
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class AsPreferencesDataSourceTest{
    private val userPreferencesSerializer = UserPreferencesSerializer()

    @Test
    fun defaultUserPreferences_isEmpty() {
//        assertEquals(
//            userPreferencesSerializer.defaultValue.autoMerge,
//        )
    }

    @Test
    fun writingAndReadingUserPreferences_outputsCorrectValue() = runTest {
        val expectedUserPreferences = userPreferences {
            followedTopicIds.put("0", true)
            followedTopicIds.put("1", true)
        }

        val outputStream = ByteArrayOutputStream()

        expectedUserPreferences.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        val actualUserPreferences = userPreferencesSerializer.readFrom(inputStream)

        assertEquals(
            expectedUserPreferences,
            actualUserPreferences,
        )
    }

    @Test(expected = CorruptionException::class)
    fun readingInvalidUserPreferences_throwsCorruptionException() = runTest {
        userPreferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}