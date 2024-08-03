package com.imcys.bilibilias.okdownloader

abstract class EventListener {

    open fun callHit(call: Download.Call) {}

    open fun callStart(call: Download.Call) {}

    open fun callCanceled(call: Download.Call) {}

    open fun callSuccess(call: Download.Call, response: Download.Response) {}

    open fun callFailed(call: Download.Call, response: Download.Response) {}

    open fun callEnd(call: Download.Call) {}

    fun interface Factory {
        fun create(call: Download.Call): EventListener
    }

    companion object {
        @JvmField
        val NONE: EventListener = object : EventListener() {
        }
    }
}

internal fun EventListener.asFactory() = EventListener.Factory { this }
