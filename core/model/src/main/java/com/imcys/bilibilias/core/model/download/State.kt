package com.imcys.bilibilias.core.model.download

public enum class State(val cn: String) {
    START("开始"),
    PENDING("等待"),
    RUNNING("运行中"),
    COMPLETED("完成"),
    ERROR("发生错误"),
}
