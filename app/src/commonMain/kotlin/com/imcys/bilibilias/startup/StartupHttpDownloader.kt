package com.imcys.bilibilias.startup

import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.startup.Startup

class StartupHttpDownloader(private val httpDownloader: HttpDownloader) : Startup {
    override suspend fun initialize() {
        httpDownloader.init()
    }
}