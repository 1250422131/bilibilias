package com.imcys.bilibilias.core.download

sealed interface TaskResult {
    data object Success : TaskResult
    data object Failure : TaskResult
}
