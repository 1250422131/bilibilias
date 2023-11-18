package com.imcys.bilibilias.okdownloader.android

import android.content.Context
import com.imcys.bilibilias.okdownloader.Download
import com.imcys.bilibilias.okdownloader.DownloadException
import com.imcys.bilibilias.okdownloader.ErrorCode
import com.imcys.bilibilias.okdownloader.Interceptor

class NetworkInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Download.Response {
        val request = chain.request()
        if (!NetworkMonitor.getInstance(context).isNetworkAvailable()) {
            throw DownloadException(ErrorCode.NET_DISCONNECT, "Network not available")
        }
        if (request is DownloadRequest) {
            when {
                (request.network and DownloadRequest.NETWORK_DATA) != DownloadRequest.NETWORK_DATA &&
                    NetworkMonitor.getInstance(context).isMobileConnected() -> {
                    throw DownloadException(
                        ErrorCode.NETWORK_NOT_ALLOWED,
                        "Expect network ${request.network}, but active network is ${
                            NetworkMonitor.getInstance(context).getActiveNetworkType()
                        }"
                    )
                }
                (request.network and DownloadRequest.NETWORK_WIFI) != DownloadRequest.NETWORK_WIFI &&
                    NetworkMonitor.getInstance(context).isWifiConnected() -> {
                    throw DownloadException(
                        ErrorCode.NETWORK_NOT_ALLOWED,
                        "Expect network ${request.network}, but active network is ${
                            NetworkMonitor.getInstance(context).getActiveNetworkType()
                        }"
                    )
                }
            }
        }
        return chain.proceed(chain.request())
    }
}
