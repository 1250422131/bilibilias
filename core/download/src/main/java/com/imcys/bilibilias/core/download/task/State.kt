package com.imcys.bilibilias.core.download.task

public enum class State(val cn: String) {
    PENDING("等待"),
    RUNNING("运行中"),
    COMPLETED("完成"),
    IDLE("空闲"),

    // may completed, but no filename can't ensure.
    UNKNOWN("未知")
}
