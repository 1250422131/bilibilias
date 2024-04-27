package com.imcys.bilibilias.common.base.extend

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun Call.awaitResponse(): Response {
    return suspendCancellableCoroutine {
        it.invokeOnCancellation {
            // 当协程被取消的时候，取消网络请求
            cancel()
        }

        enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                it.resume(response) {}
            }
        })
    }
}
