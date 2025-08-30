package com.imcys.bilibilias.core.ffmpeg

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.ProgressHolder
import androidx.media3.transformer.Transformer
import com.imcys.bilibilias.core.flow.interval
import com.imcys.bilibilias.core.logging.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.io.File

@OptIn(UnstableApi::class)
internal class Media3MediaMultiplexer(
    private val context: Context,
) : MediaMultiplexer {
    private val logger = logger<Media3MediaMultiplexer>()

    override val progress = MutableStateFlow(0)
    override val isRunning = MutableStateFlow(false)
    override suspend fun muxMedia(inputPaths: List<String>, outputPath: String) {
        val tempFile = File(context.filesDir, "output.mp4")
        val sequences = inputPaths.map {
            val editedMediaItem = EditedMediaItem.Builder(MediaItem.fromUri(it)).build()
            EditedMediaItemSequence.Builder(editedMediaItem).build()
        }
        val composition = Composition.Builder(sequences)
            .setTransmuxVideo(true)
            .setTransmuxAudio(true)
            .build()

        val transformer = Transformer.Builder(context)
            .addListener(
                object : Transformer.Listener {
                    override fun onCompleted(
                        composition: Composition,
                        exportResult: ExportResult
                    ) {
                        setRunState(false)
                        logger.info { "onCompleted" }
                        copyFile(tempFile, Uri.parse(outputPath))
                    }

                    override fun onError(
                        composition: Composition,
                        exportResult: ExportResult,
                        exportException: ExportException
                    ) {
                        logger.error(exportException) { "onError" }
                        setRunState(false)
                    }
                }
            )
            .build()
        withContext(Dispatchers.Main) {
            transformer.start(composition, tempFile.absolutePath)
            setRunState(true)
            val progressHolder = ProgressHolder()
            launch {
                interval(500).collect {
                    val progressState = transformer.getProgress(progressHolder)
                    progress.update { progressState }
                }
            }
        }
    }

    private fun setRunState(state: Boolean) {
        isRunning.value = state
    }

    private fun copyFile(sourcePath: File, destinationUri: Uri) {
        val outputStream = context.contentResolver.openOutputStream(destinationUri, "rw")
            ?: throw IOException("Failed to open output stream for URI: $destinationUri")
        sourcePath.inputStream().buffered().use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }
}