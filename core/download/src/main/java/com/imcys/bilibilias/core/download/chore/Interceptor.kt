package com.imcys.bilibilias.core.download.chore

import com.imcys.bilibilias.core.download.task.GroupTask

interface Interceptor {
    val enable: Boolean
        get() = false

    fun intercept(chain: Chain)

    companion object {
        operator fun invoke(block: (chain: Chain) -> Unit): Interceptor =
            Interceptor { block(it) }
    }

    interface Chain {
        fun request(): GroupTask
        fun proceed(groupTask: GroupTask)
    }
}
