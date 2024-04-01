package com.imcys.bilibilias.base.utils

import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.imcys.bilibilias.home.ui.fragment.tool.DownloadFileRequest
import io.github.aakira.napier.Napier
import javax.inject.Inject

class FileDownloadFactory @Inject constructor(private val videoRepository: VideoRepository) {

    fun download(aid: DownloadFileRequest) {
        Napier.d { "$aid" }
    }
}

