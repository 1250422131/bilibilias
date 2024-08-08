package com.imcys.bilibilias.core.datastore.preferences

import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.InMemoryDataStore
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AsPreferencesDataSourceTest {
    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: AsPreferencesDataSource

    @Before
    fun setup() {
        subject = AsPreferencesDataSource(InMemoryDataStore(UserPreferences()))
    }

    @Test
    fun storageFolderPathIsNullByDefault() = testScope.runTest {
        assertNull(subject.userData.first().storageFolder)
    }

    @Test
    fun changeStorageFolder_IsNotNull() = testScope.runTest {
        val testPath = "test"
        subject.setStorageFolder(testPath)

        assertEquals(testPath, subject.userData.first().storageFolder)
    }

    @Test
    fun ffmpegCommandIsNullByDefault() = testScope.runTest {
        assertNull(subject.userData.first().ffmpegCommand)
    }
}
