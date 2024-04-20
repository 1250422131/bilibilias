package com.imcys.bilibilias.core.download.task

public enum class State {
    PENDING,
    RUNNING,
    COMPLETED,
    IDLE,

    // may completed, but no filename can't ensure.
    UNKNOWN
}
