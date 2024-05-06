package com.imcys.bilibilias.core.download

import android.content.Context
import androidx.collection.mutableObjectListOf
import com.imcys.bilibilias.core.model.download.CacheFile
import com.imcys.bilibilias.core.model.download.Entry
import com.imcys.bilibilias.core.model.video.ViewInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Singleton
class FileManage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    private val fileDownload: FileDownload,
) {
    private val completedQueue = mutableObjectListOf<CacheFile>()

    init {
        val result = mutableListOf<MutableList<File>>()
        try {
            context.downloadDir.walkBottomUp().onEnter {
                if (it.name.startsWith("c_")) {
                    result.add(mutableListOf())
                }
                true
            }.filter { it.isFile }.forEach {
                result.last().add(it)
            }
            result.map {
                val entry: Entry = it.single { it.name == "entry.json" }.let(File::inputStream)
                    .let(json::decodeFromStream)
                CacheFile(
                    ViewInfo(
                        entry.avid,
                        entry.bvid,
                        entry.pageData.cid,
                        entry.pageData.downloadTitle
                    ),
                    it.find { it.name == "video.mp4" },
                    it.find { it.name == "audio.mp4" },
                    it.find { it.name == "danmaku.xml" }
                )
            }.forEach(completedQueue::add)
        } catch (e: Exception) {
            Napier.d(e) { "" }
        }
    }

    fun download(request: DownloadRequest) {
        fileDownload.download(request)
    }
    /**
     * 2024-04-22 15:50:06.831  6050-6050  FileManage$<init>       com.imcys.bilibilias                 D  [
     * [/data/user/0/com.imcys.bilibilias/download/1053408147/c_1510498679/entry.json,
     * /data/user/0/com.imcys.bilibilias/download/1053408147/c_1510498679/112/video.mp4,
     * /data/user/0/com.imcys.bilibilias/download/1053408147/c_1510498679/112/audio.mp4,
     * /data/user/0/com.imcys.bilibilias/download/1053408147/c_1510498679/112,
     * /data/user/0/com.imcys.bilibilias/download/1053408147/c_1510498679/danmaku.xml,
     * /data/user/0/com.imcys.bilibilias/download/1053408147/c_1510498679,
     * /data/user/0/com.imcys.bilibilias/download/1053408147],
     *
     * [/data/user/0/com.imcys.bilibilias/download/1651579701/c_1466484145/116/video.mp4,
     * /data/user/0/com.imcys.bilibilias/download/1651579701/c_1466484145/116,
     * /data/user/0/com.imcys.bilibilias/download/1651579701/c_1466484145,
     * /data/user/0/com.imcys.bilibilias/download/1651579701,
     * /data/user/0/com.imcys.bilibilias/download]
     *
     * ]
     */
}
