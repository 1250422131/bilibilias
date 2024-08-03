package com.imcys.common.appinitializer

enum class AppInitializerStartType {
    /**
     * 串行执行
     */
    TYPE_SERIES,

    /**
     * 并发执行
     */
    TYPE_PARALLEL,
}
