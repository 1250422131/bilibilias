package com.imcys.bilibilias.core.download.chore

import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import io.github.aakira.napier.Napier
import javax.inject.Inject

class DefaultGroupTaskCall @Inject constructor(
    private val mixingInterceptor: MixingInterceptor,
) {

    private fun getResponseWithInterceptorChain(tasks: List<DownloadTaskEntity>) {
        val interceptors = mutableListOf<Interceptor<*>>()
        interceptors += mixingInterceptor
        val chain = Interceptor.Chain(interceptors, 0)
        return chain.proceed(tasks)
    }

    fun execute(tasks: List<DownloadTaskEntity>) {
        try {
            Napier.d { tasks.joinToString("\n") }
            getResponseWithInterceptorChain(tasks)
        } finally {
        }
    }
}
