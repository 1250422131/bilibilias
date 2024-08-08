package com.imcys.bilibilias.core.datastore.preferences

import androidx.datastore.core.CorruptionException
import com.imcys.bilibilias.core.datastore.UserPreferencesSerializer
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class UserPreferencesSerializerTest {

    private val userPreferencesSerializer = UserPreferencesSerializer(UnconfinedTestDispatcher())

    @Test
    fun defaultUserPreferences_isEmpty() {
        assertEquals(
            UserPreferences(),
            userPreferencesSerializer.defaultValue,
        )
    }

    @Test
    fun writingAndReadingUserPreferences_outputsCorrectValue() = runTest {
        val expectedUserPreferences = UserPreferences()

        val outputStream = ByteArrayOutputStream()

        userPreferencesSerializer.writeTo(expectedUserPreferences, outputStream)

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
