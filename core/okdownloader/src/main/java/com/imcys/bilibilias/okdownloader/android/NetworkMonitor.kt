package com.imcys.bilibilias.okdownloader.android

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

@SuppressLint("MissingPermission")
class NetworkMonitor private constructor(
    private val mContext: Context
) : BaseObservable<NetworkMonitor.NetworkRecord>() {

    private val mHandler = Handler(Looper.getMainLooper())

    companion object {
        const val NETWORK_NONE = -1
        const val NETWORK_WIFI = 1
        const val NETWORK_MOBILE = 2
        const val NETWORK_OTHER = 3
        const val NETWORK_ETHERNET = 4

        @Volatile
        private var instance: NetworkMonitor? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: NetworkMonitor(context.applicationContext).also { instance = it }
        }
    }

    private var mRunning: AtomicBoolean = AtomicBoolean(false)
    private val mConnectivityManager by lazy { mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    private var mCurrentNetworkType: AtomicInteger = AtomicInteger(NETWORK_NONE)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val mNetworkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            onNetworkChanged()
        }

        override fun onUnavailable() {
            super.onUnavailable()
            onNetworkChanged()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            onNetworkChanged()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            onNetworkChanged()
        }
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onNetworkChanged()
        }
    }

    private val mNetworkChangedRunnable = Runnable {
        val previous = mCurrentNetworkType.getAndSet(getActiveNetworkType())
        notifyObservers(
            NetworkRecord(
                NetworkRecord.Item(previous, previous != NETWORK_NONE),
                NetworkRecord.Item(
                    mCurrentNetworkType.get(),
                    mCurrentNetworkType.get() != NETWORK_NONE
                ),
            )
        )
    }

    private fun onNetworkChanged() {
        mHandler.removeCallbacks(mNetworkChangedRunnable)
        mHandler.postDelayed(mNetworkChangedRunnable, 1000)
    }

    fun getActiveNetworkType(): Int {
        val networkInfo = getActiveNetworkInfo() ?: return NETWORK_NONE
        if (!networkInfo.isConnected) return NETWORK_NONE
        return when (networkInfo.type) {
            ConnectivityManager.TYPE_WIFI -> NETWORK_WIFI
            ConnectivityManager.TYPE_MOBILE -> NETWORK_MOBILE
            ConnectivityManager.TYPE_ETHERNET -> NETWORK_ETHERNET
            else -> NETWORK_OTHER
        }
    }

    private fun getActiveNetworkInfo(): NetworkInfo? {
        mConnectivityManager.activeNetwork?.let {
            return mConnectivityManager.getNetworkInfo(it)
        }
        return mConnectivityManager.activeNetworkInfo
    }

    fun startup() {
        if (mRunning.getAndSet(true)) return
        mCurrentNetworkType.getAndSet(getActiveNetworkType())
        mConnectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            mNetworkCallback
        )
    }

    fun isWifiConnected(): Boolean {
            mConnectivityManager.activeNetwork?.let {
                val networkInfo = mConnectivityManager.getNetworkInfo(it)
                if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    return networkInfo.isConnected
                }
                return false
            }
        return mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)?.isConnected
            ?: false
    }

    fun isMobileConnected(): Boolean {
        mConnectivityManager.activeNetwork?.let {
            val networkInfo = mConnectivityManager.getNetworkInfo(it)
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return networkInfo.isConnected
            }
            return false
        }
        return mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)?.isConnected
            ?: false
    }

    fun isNetworkAvailable(): Boolean = getActiveNetworkType() != NETWORK_NONE

    fun shutdown() {
        if (!mRunning.getAndSet(false)) return
        mConnectivityManager.unregisterNetworkCallback(mNetworkCallback)
    }

    data class NetworkRecord(val previous: Item, val current: Item) {
        data class Item(
            val type: Int,
            val available: Boolean
        )
    }
}
