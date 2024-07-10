package com.imcys.bilibilias.core.testing.util

import com.imcys.bilibilias.core.data.util.ErrorMessage
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestErrorMonitor(networkMonitor: TestNetworkMonitor) : ErrorMonitor {
    override var offlineMessage: String? = "offline"
    override val isOffline: Flow<Boolean> = networkMonitor.isOnline.map { !it }
    override fun addShortErrorMessage(
        error: String,
        label: String?,
        successAction: (() -> Unit)?,
        failureAction: (() -> Unit)?,
    ): String? {
        return "1"
    }

    override fun addLongErrorMessage(
        error: String,
        label: String?,
        successAction: (() -> Unit)?,
        failureAction: (() -> Unit)?,
    ): String? {
        return "2"
    }

    override fun addIndefiniteErrorMessage(
        error: String,
        label: String?,
        successAction: (() -> Unit)?,
        failureAction: (() -> Unit)?,
    ): String? {
        return "3"
    }

    override fun clearErrorMessage(id: String) {
        // Do nothing
    }

    override val errorMessage: Flow<ErrorMessage?>
        get() = flowOf(ErrorMessage("Error Message", "1"))
}
