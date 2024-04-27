package com.imcys.bilibilias.core.download.chore

import io.github.aakira.napier.Napier

interface Interceptor<T> {
    val enable: Boolean
        get() = false

    fun intercept(message: T, chain: Chain)
    class Chain(
        private val interceptors: List<Interceptor<*>>,
        private val index: Int = 0
    ) {
        fun proceed(message: Any) {
            val next = Chain(interceptors, index + 1)
            try {
                (interceptors.getOrNull(index) as? Interceptor<Any>)?.intercept(message, next)
            } catch (e: Exception) {
                Napier.d(e, "Chain") { "Chain.proceed[$message]" }
            }
        }
    }
}
