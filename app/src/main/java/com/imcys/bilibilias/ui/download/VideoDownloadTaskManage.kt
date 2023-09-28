package com.imcys.bilibilias.ui.download

import android.os.Environment
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import com.liulishuo.okdownload.kotlin.listener.onDownloadFromBeginning
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareGet
import io.ktor.http.HttpHeaders
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoDownloadTaskManage

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val httpClient: HttpClient,
) : BaseViewModel() {
    /**
     * qn 该值在 DASH 格式下无效，因为 DASH 格式会取到所有分辨率的流地址
     * flv
     * "${BilibiliApi.videoPlayPath}?bvid=${videoDetails.bvid}&cid=${it.cid}&qn=$qn&fnval=0&fourk=1"
     * dash
     * "${BilibiliApi.videoPlayPath}?bvid=${videoDetails.bvid}&cid=${it.cid}&qn=$qn&fnval=4048&fourk=1"
     */
    fun downloadVideo(bvid: String, cid: Long, url: String, qn: Int = 80, flvOrDash: Int = 0) {
        val photoDir = File(Environment.getExternalStorageDirectory(), "BILIBILIAS")
        val file = File(photoDir, "test.flv")
        launchIO {
            httpClient.prepareGet(url) {
                header(HttpHeaders.Referrer, "https://www.bilibili.com")
            }.execute { httpResponse ->
                val channel: ByteReadChannel = httpResponse.body()
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                    while (!packet.isEmpty) {
                        val bytes = packet.readBytes()
                        file.appendBytes(bytes)
                        Timber.tag("download").d("Received ${file.length()} bytes from ${httpResponse.contentLength()}")
                    }
                }
            }
        }
    }
}
