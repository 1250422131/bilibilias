package com.imcys.bilibilias.server

import android.app.Service
import android.content.Intent
import android.os.IBinder

class DownloadAsService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}