package com.imcys.bilibilias.download

import android.app.Application
import com.imcys.bilibilias.common.utils.download.CCJsonToAss
import com.imcys.bilibilias.common.utils.download.CCJsonToSrt
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.download.CCFileType
import com.imcys.bilibilias.data.model.download.lowercase
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.video.BILIVideoCCInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfoV2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * 字幕下载器
 * 负责字幕下载、转换和保存
 */
class SubtitleDownloader(
    private val videoInfoRepository: VideoInfoRepository,
    private val fileOutputManager: FileOutputManager,
    private val context: Application
) {
    /**
     * 下载字幕用于嵌入视频
     */
    suspend fun downloadSubtitlesForEmbed(
        videoPlayerInfoV2: NetWorkResult<BILIVideoPlayerInfoV2?>,
        segmentId: Long
    ): List<LocalSubtitle> = withContext(Dispatchers.IO) {
        val localSubtitles = mutableListOf<LocalSubtitle>()

        videoPlayerInfoV2.data?.subtitle?.subtitles?.forEach { subtitle ->
            val url = subtitle.finalSubtitleUrl
            val finalUrl = if (!url.contains("https")) "https:" else ""
            val language = subtitle.lan
            val langDoc = subtitle.lanDoc

            runCatching {
                videoInfoRepository.getVideoCCInfo((finalUrl + url).toHttps())
            }.onSuccess { ccInfo ->
                val fileContentStr = CCJsonToSrt.jsonToSrt(ccInfo)
                val tempDir = File(context.externalCacheDir, "cc")
                if (!tempDir.exists()) tempDir.mkdirs()

                val tempFile = File(tempDir, "embed_cc_${segmentId}_${language}.srt")
                FileOutputStream(tempFile).use { outputStream ->
                    outputStream.write(fileContentStr.toByteArray(Charsets.UTF_8))
                    outputStream.flush()
                }

                localSubtitles.add(LocalSubtitle(language, langDoc, tempFile.absolutePath))
            }
        }

        localSubtitles
    }

    /**
     * 下载字幕到下载目录
     */
    suspend fun downloadSubtitlesToFile(
        videoPlayerInfoV2: NetWorkResult<BILIVideoPlayerInfoV2?>,
        title: String,
        ccFileType: CCFileType
    ) = withContext(Dispatchers.IO) {
        videoPlayerInfoV2.data?.subtitle?.subtitles?.forEach { cc ->
            val url = cc.finalSubtitleUrl
            val finalUrl = if (!url.contains("https")) "https:" else ""
            val videoCCInfo = videoInfoRepository.getVideoCCInfo((finalUrl + url).toHttps())
            val content = convertCc(videoCCInfo, ccFileType)
            val fileName = "${title}_${cc.lan}_${ccFileType.lowercase()}"

            val subtitleType = when (ccFileType) {
                CCFileType.ASS -> FileOutputManager.SubtitleType.ASS
                CCFileType.SRT -> FileOutputManager.SubtitleType.SRT
            }

            fileOutputManager.createSubtitleOutputStream(fileName, subtitleType)
                .use { it.write(content.toByteArray(Charsets.UTF_8)) }
        }
    }

    private fun convertCc(cc: BILIVideoCCInfo, type: CCFileType): String = when (type) {
        CCFileType.ASS -> CCJsonToAss.jsonToAss(cc, "字幕", "1920", "1080")
        CCFileType.SRT -> CCJsonToSrt.jsonToSrt(cc)
    }
}
