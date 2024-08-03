package com.imcys.bilibilias.okdownloader.internal.core

import com.imcys.bilibilias.okdownloader.Download
import java.util.concurrent.CopyOnWriteArrayList
internal class Dispatcher : Download.Subjection, Download.Subscriber {

    private val observers: MutableMap<String, MutableSet<Download.Subscriber>> = HashMap()
    private val globalObservers = CopyOnWriteArrayList<Download.Subscriber>()

    override fun subscribe(subscriber: Download.Subscriber) {
        globalObservers += subscriber
    }

    override fun subscribe(url: String, subscriber: Download.Subscriber) {
        synchronized(observers) {
            observers.getOrPut(url) { mutableSetOf() } += subscriber
        }
    }

    override fun unsubscribe(subscriber: Download.Subscriber) {
        globalObservers -= subscriber
    }

    override fun unsubscribe(url: String, subscriber: Download.Subscriber) {
        synchronized(observers) {
            observers.getOrPut(url) { mutableSetOf() } -= subscriber
        }
    }

    private fun Download.Call.dispatch(block: Download.Subscriber.() -> Unit) {
        synchronized(observers) {
            observers[request.url]?.forEach(block)
        }
        globalObservers.forEach(block)
    }

    override fun onSuccess(call: Download.Call, response: Download.Response) {
        call.dispatch { onSuccess(call, response) }
    }

    override fun onFailure(call: Download.Call, response: Download.Response) {
        call.dispatch { onFailure(call, response) }
    }
}
