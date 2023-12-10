package com.bilias.crash.policy

import com.bilias.crash.Info
import com.bilias.crash.group.Group

/**
 * 使用不同的策略来分发信息
 */
interface Policy<I : Info> {
    fun report(info: I, group: Group)
    val group: Group
}