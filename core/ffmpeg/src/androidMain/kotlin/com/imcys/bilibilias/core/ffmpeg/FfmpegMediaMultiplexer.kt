package com.imcys.bilibilias.core.ffmpeg

import android.net.Uri
import com.antonkarpenko.ffmpegkit.FFmpegKit
import com.antonkarpenko.ffmpegkit.FFmpegKitConfig
import com.antonkarpenko.ffmpegkit.ReturnCode
import com.antonkarpenko.ffmpegkit.SessionState
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.logging.logger
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

internal class FfmpegMediaMultiplexer : MediaMultiplexer {
    override val isRunning = MutableStateFlow(false)
    override val progress = MutableStateFlow(0)
    private val logger = logger<MediaMultiplexer>()
    override suspend fun muxMedia(
        inputPaths: List<String>,
        outputPath: String
    ) {
        logger.debug { "Input: " + inputPaths.joinToString() + " OutPutPath: $outputPath" }
        val context = KmpContext.get()
        val inputs = inputPaths.map { path ->
            FFmpegKitConfig.getSafParameterForRead(
                context,
                Uri.fromFile(File(path))
            )
        }
        val output = FFmpegKitConfig.getSafParameterForWrite(context, Uri.parse(outputPath))
        val command = inputs.joinToString(
            " ",
            " ",
            " "
        ) { "-i $it" } + " -c:v copy -c:a copy " + output
        logger.debug { "Command $command" }
        FFmpegKit.executeAsync(command) { session ->
            val state: SessionState = session.getState()
            setSessionState(state)
            val returnCode: ReturnCode = session.getReturnCode()

            if (returnCode.isValueSuccess) {
                logger.info { "Ffmpeg process exited with state $state and Success" }
            } else if (returnCode.isValueError) {
                logger.error { "Ffmpeg process exited with state $state and rc Error." }
            } else {
                logger.warn { "Ffmpeg process exited with state $state and rc Cancle." }
            }
        }
    }

    private fun setSessionState(sessionState: SessionState) {
        when (sessionState) {
            SessionState.RUNNING -> isRunning.value = true

            SessionState.CREATED,
            SessionState.FAILED,
            SessionState.COMPLETED -> isRunning.value = false

        }
    }
}