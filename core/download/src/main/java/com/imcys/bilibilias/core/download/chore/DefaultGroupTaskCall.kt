package com.imcys.bilibilias.core.download.chore

import com.imcys.bilibilias.core.download.task.GroupTask
import javax.inject.Inject

class DefaultGroupTaskCall @Inject constructor(
    private val mixingInterceptor: MixingInterceptor,
    private val moveFileInterceptor: MoveFileInterceptor,
) {
    fun execute(groupTask: GroupTask) {
        try {
            getResponseWithInterceptorChain(groupTask)
        } finally {
        }
    }

    private fun getResponseWithInterceptorChain(groupTask: GroupTask) {
        val interceptors = mutableListOf<Interceptor<*>>()
        interceptors += mixingInterceptor
        val chain = Interceptor.Chain(interceptors, 0)
        return chain.proceed(groupTask)
    }
}
