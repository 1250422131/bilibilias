package com.imcys.bilibilias.core.media.sync

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.imcys.bilibilias.core.common.network.AsDispatchers
import com.imcys.bilibilias.core.common.network.Dispatcher
import com.imcys.bilibilias.core.network.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class LocalMediaInfoSynchronizer @Inject constructor(
    // private val mediumDao: MediumDao,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    @Dispatcher(AsDispatchers.Default) private val dispatcher: CoroutineDispatcher,
) : MediaInfoSynchronizer {

    private val media = MutableSharedFlow<Uri>()

    override suspend fun addMedia(uri: Uri) = media.emit(uri)

    private suspend fun sync(): Unit = withContext(dispatcher) {
        // media.collect { mediumUri ->
        //     val medium = mediumDao.getWithInfo(mediumUri.toString()) ?: return@collect
        //     if (medium.mediumEntity.thumbnailPath?.let { File(it) }?.exists() == true) {
        //         return@collect
        //     }
        //
        //     val mediaInfo = runCatching {
        //         MediaInfoBuilder().from(context = context, uri = mediumUri).build() ?: throw NullPointerException()
        //     }.onFailure { e ->
        //         e.printStackTrace()
        //         Log.d(TAG, "sync: MediaInfoBuilder exception", e)
        //     }.getOrNull() ?: return@collect
        //
        //     val thumbnail = runCatching { mediaInfo.getFrame() }.getOrNull()
        //     mediaInfo.release()
        //
        //     val videoStreamInfo = mediaInfo.videoStream?.toVideoStreamInfoEntity(medium.mediumEntity.uriString)
        //     val audioStreamsInfo = mediaInfo.audioStreams.map {
        //         it.toAudioStreamInfoEntity(medium.mediumEntity.uriString)
        //     }
        //     val subtitleStreamsInfo = mediaInfo.subtitleStreams.map {
        //         it.toSubtitleStreamInfoEntity(medium.mediumEntity.uriString)
        //     }
        //     val thumbnailPath = thumbnail?.saveTo(
        //         storageDir = context.thumbnailCacheDir,
        //         quality = 40,
        //         fileName = medium.mediumEntity.mediaStoreId.toString(),
        //     )
        //
        //     mediumDao.upsert(
        //         medium.mediumEntity.copy(
        //             format = mediaInfo.format,
        //             thumbnailPath = thumbnailPath,
        //         ),
        //     )
        //     videoStreamInfo?.let { mediumDao.upsertVideoStreamInfo(it) }
        //     audioStreamsInfo.onEach { mediumDao.upsertAudioStreamInfo(it) }
        //     subtitleStreamsInfo.onEach { mediumDao.upsertSubtitleStreamInfo(it) }
        // }
    }

    init {
        applicationScope.launch { sync() }
    }

    companion object {
        private const val TAG = "MediaInfoSynchronizer"
    }
}

// private fun VideoStream.toVideoStreamInfoEntity(mediumUri: String) = VideoStreamInfoEntity(
//     index = index,
//     title = title,
//     codecName = codecName,
//     language = language,
//     disposition = disposition,
//     bitRate = bitRate,
//     frameRate = frameRate,
//     frameWidth = frameWidth,
//     frameHeight = frameHeight,
//     mediumUri = mediumUri,
// )

// private fun AudioStream.toAudioStreamInfoEntity(mediumUri: String) = AudioStreamInfoEntity(
//     index = index,
//     title = title,
//     codecName = codecName,
//     language = language,
//     disposition = disposition,
//     bitRate = bitRate,
//     sampleFormat = sampleFormat,
//     sampleRate = sampleRate,
//     channels = channels,
//     channelLayout = channelLayout,
//     mediumUri = mediumUri,
// )
//
// private fun SubtitleStream.toSubtitleStreamInfoEntity(mediumUri: String) = SubtitleStreamInfoEntity(
//     index = index,
//     title = title,
//     codecName = codecName,
//     language = language,
//     disposition = disposition,
//     mediumUri = mediumUri,
// )

suspend fun Bitmap.saveTo(
    storageDir: File,
    quality: Int = 100,
    fileName: String,
): String? = withContext(Dispatchers.IO) {
    val thumbFile = File(storageDir, fileName)
    try {
        FileOutputStream(thumbFile).use { fos ->
            compress(Bitmap.CompressFormat.JPEG, quality, fos)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    if (thumbFile.exists()) thumbFile.path else null
}
