package com.imcys.bilibilias

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.logging.logger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DownloadService : Service() {
    private val scope = CoroutineScope(
        Dispatchers.Default + CoroutineName("DownloadService") + SupervisorJob(),
    )
    private lateinit var httpDownloader: HttpDownloader
    private val logger = logger<DownloadService>()
    override fun onCreate() {
        scope.launch {
            httpDownloader.downloadStatesFlow.map {

            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (intent?.getBooleanExtra(INTENT_STOP_EXTRA, false) == true) {
//            stopSelf()
//            return super.onStartCommand(intent, flags, startId)
//        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}