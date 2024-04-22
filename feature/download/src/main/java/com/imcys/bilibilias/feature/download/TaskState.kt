package com.imcys.bilibilias.feature.download

import pro.respawn.flowmvi.api.MVIState

sealed interface TaskState : MVIState {
    data object Loading : TaskState
}