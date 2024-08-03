package com.imcys.bilibilias.tool

import androidx.lifecycle.SavedStateHandle
import com.imcys.network.fake.FakeVideoDataSources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ToolViewModelTest : TestWatcher() {

    private val viewModel = ToolViewModel(FakeVideoDataSources(), SavedStateHandle())
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Test
    fun `givenText_thenReturnSearchType`() {

    }

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}