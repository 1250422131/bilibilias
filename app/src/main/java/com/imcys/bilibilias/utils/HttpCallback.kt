package com.imcys.bilibilias.utils

import android.util.Log
import com.imcys.bilibilias.base.utils.asLogD
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class HttpCallback(val method: (data: String) -> Unit) : Callback {
    override fun onFailure(call: Call, e: IOException) {
       Log.d("异常","请求异常")
    }

    override fun onResponse(call: Call, response: Response) {
        if (response.isSuccessful) {
            method(response.body!!.string())
        }
    }
}