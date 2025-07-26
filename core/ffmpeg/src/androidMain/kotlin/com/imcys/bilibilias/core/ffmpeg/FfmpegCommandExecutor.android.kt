package com.imcys.bilibilias.core.ffmpeg

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.DefaultDecoderFactory
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.TransformationRequest
import androidx.media3.transformer.Transformer
import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.context.KmpContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(UnstableApi::class)
internal actual class FfmpegCommandImpl : FfmpegCommand {
    private val logger = Logger.withTag("FfmpegCommandImpl")

    actual override suspend fun execute(command: String) {
    }

    actual override suspend fun execute(command: List<String>) {
        val context = KmpContext.get()
        val sequences = command.take(2).map {
            val editedMediaItem = EditedMediaItem.Builder(MediaItem.fromUri(it)).build()
            EditedMediaItemSequence.Builder(editedMediaItem).build()
        }
        logger.i { "Size ${sequences.size}" }
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
                        logger.i { "onCompleted" }
                    }

                    override fun onError(
                        composition: Composition,
                        exportResult: ExportResult,
                        exportException: ExportException
                    ) {
                        logger.e(exportException) { "onError" }
                    }

                    override fun onFallbackApplied(
                        composition: Composition,
                        originalTransformationRequest: TransformationRequest,
                        fallbackTransformationRequest: TransformationRequest
                    ) {
                        logger.i { "onFallbackApplied" }
                    }
                }
            )
            .build()
        DefaultDecoderFactory.Builder(context).build()
        withContext(Dispatchers.Main) {
            transformer.start(composition, command[2])
        }
    }
}