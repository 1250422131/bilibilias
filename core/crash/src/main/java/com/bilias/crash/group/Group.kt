package com.bilias.crash.group

/**
 * 按分组来分发
 */
interface Group {
    /**
     * 分组的名称
     */
    val name: String

    /**
     * 判断是否符合该组规则
     */
    fun counts(): Boolean
}