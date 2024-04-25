package com.imcys.bilibilias.core.download.chore

import com.imcys.bilibilias.core.download.task.GroupTask

internal class DefaultInterceptorChain(
    private val index: Int,
    private val request: GroupTask,
    private val interceptors: List<Interceptor>
) : Interceptor.Chain {

    private fun copy(index: Int) = DefaultInterceptorChain(index, request, interceptors)
    override fun request(): GroupTask {
        return request
    }

    override fun proceed(groupTask: GroupTask) {
        check(index < interceptors.size)
        return interceptors[index].intercept(copy(index + 1))
    }
}
