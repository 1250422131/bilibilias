package com.imcys.bilibilias.common.base.utils.http

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class HttpCallback(val method: (data: String) -> Unit) : Callback {
    override fun onFailure(call: Call, e: IOException) {

    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            method(response.body?.string() ?: "")
        }
    }
}