package com.imcys.bilibilias.okdownloader
fun interface Interceptor {

    @Throws(DownloadException::class)
    fun intercept(chain: Chain): Download.Response

    companion object {
        /**
         * Constructs an interceptor for a lambda. This compact syntax is most useful for inline
         * interceptors.
         *
         * ```
         * val interceptor = Interceptor { chain: Interceptor.Chain ->
         *     chain.proceed(chain.request())
         * }
         * ```
         */
        inline operator fun invoke(crossinline block: (chain: Chain) -> Download.Response): Interceptor =
            Interceptor { block(it) }
    }

    interface Chain {
        fun request(): Download.Request
        fun call(): Download.Call
        fun callback(): Download.Callback
        @Throws(DownloadException::class)
        fun proceed(request: Download.Request): Download.Response
    }
}
