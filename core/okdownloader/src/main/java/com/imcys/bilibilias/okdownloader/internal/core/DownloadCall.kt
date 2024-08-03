package com.imcys.bilibilias.okdownloader.internal.core

import com.imcys.bilibilias.okdownloader.Download
import com.imcys.bilibilias.okdownloader.Downloader
import com.imcys.bilibilias.okdownloader.EventListener
import com.imcys.bilibilias.okdownloader.Interceptor
import okhttp3.Call
import java.util.ServiceLoader
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean
internal interface InternalCall : Download.Call {

    var httpCall: Call?
    fun isCancelSafely(): Boolean
}

internal class DefaultDownloadCall(
    private val client: Downloader,
    private val originalRequest: Download.Request
) : InternalCall {

    private val eventListener: EventListener = client.eventListenerFactory.create(this)
    private val canceled = AtomicBoolean(false)
    private val canceledSafely = AtomicBoolean(false)
    private val executed = AtomicBoolean(false)
    override var httpCall: Call? = null

    override val request: Download.Request get() = originalRequest

    override fun execute(): Download.Response {
        return execute(Download.Callback.NOOP)
    }

    override fun execute(callback: Download.Callback): Download.Response {
        check(executed.compareAndSet(false, true)) { "Already Executed" }
        callback.onStart(this)
        eventListener.callStart(this)
        try {
            client.downloadPool.executed(this, eventListener)
            val response = getResponseWithInterceptorChain(callback)
            if (response.isSuccessful()) {
                callback.onSuccess(this, response)
                eventListener.callSuccess(this, response)
            } else if (isCanceled()) {
                callback.onCancel(this)
                eventListener.callCanceled(this)
            } else {
                callback.onFailure(this, response)
                eventListener.callFailed(this, response)
            }
            return response
        } finally {
            eventListener.callEnd(this)
            client.downloadPool.finished(this)
        }
    }

    private fun getResponseWithInterceptorChain(callback: Download.Callback): Download.Response {
        val interceptors = mutableListOf<Interceptor>()
        interceptors += RetryInterceptor(client)
        interceptors += ExceptionInterceptor()
        interceptors += LocalExistsInterceptor()
        interceptors += SynchronousInterceptor()
        interceptors += client.interceptors
        interceptors += extInterceptors
        interceptors += VerifierInterceptor()
        interceptors += ExchangeInterceptor(client)
        val chain = DefaultInterceptorChain(0, originalRequest, this, callback, interceptors)
        return chain.proceed(originalRequest)
    }

    override fun enqueue() {
        enqueue(Download.Callback.NOOP)
    }

    override fun enqueue(callback: Download.Callback) {
        check(executed.compareAndSet(false, true)) { "Already Executed" }
        client.downloadPool.enqueue(AsyncCall(callback, request.priority), eventListener)
    }

    override fun cancel() {
        if (isCanceled()) return
        this.httpCall?.cancel()
        this.canceled.getAndSet(true)
    }

    override fun cancelSafely() {
        cancel()
        this.canceledSafely.getAndSet(true)
    }

    override fun isExecuted(): Boolean {
        return this.executed.get()
    }

    override fun isCanceled(): Boolean {
        return this.canceled.get()
    }

    override fun isCancelSafely(): Boolean {
        return this.canceledSafely.get()
    }

    override fun toString(): String {
        return "DefaultDownloadCall(request=$request)"
    }

    internal inner class AsyncCall(
        private val callback: Download.Callback,
        private val priority: Download.Priority
    ) : Runnable, Comparable<Download.Priority> {

        val call: Download.Call
            get() = this@DefaultDownloadCall

        override fun run() {
            callback.onStart(call)
            eventListener.callStart(call)
            try {
                val response = getResponseWithInterceptorChain(callback)
                if (response.isSuccessful()) {
                    callback.onSuccess(call, response)
                    eventListener.callSuccess(call, response)
                } else if (call.isCanceled()) {
                    callback.onCancel(call)
                    eventListener.callCanceled(call)
                } else {
                    callback.onFailure(call, response)
                    eventListener.callFailed(call, response)
                }
            } finally {
                eventListener.callEnd(call)
                client.downloadPool.finished(this)
            }
        }

        fun executeOn(executor: Executor) {
            var success = false
            try {
                executor.execute(this)
                success = true
            } finally {
                if (!success) {
                    client.downloadPool.finished(this)
                }
            }
        }

        override fun compareTo(other: Download.Priority): Int {
            return this.priority.ordinal - other.ordinal
        }
    }

    companion object {
        private val extInterceptors: List<Interceptor> by lazy(LazyThreadSafetyMode.NONE) {
            ServiceLoader.load(Interceptor::class.java).toList()
        }
    }
}
