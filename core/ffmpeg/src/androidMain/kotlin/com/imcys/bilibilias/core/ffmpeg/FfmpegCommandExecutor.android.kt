package com.imcys.bilibilias.core.ffmpeg

import com.github.pgreze.process.Redirect
import com.github.pgreze.process.process
import kotlinx.coroutines.flow.toList
import java.io.File
import java.util.Collections

internal actual class FfmpegCommandImpl : FfmpegCommand {
    val errLines = Collections.synchronizedList(mutableListOf<String>())
    actual override suspend fun execute(command: String) {
        val res = process(
            command,
            stdout = Redirect.ToFile(File("my-input.txt")),

            // If you want to handle this stream yourself,
            // a Flow<String> instance can be used.
            stderr = Redirect.Consume { flow -> flow.toList(errLines) },
        )
    }

    actual override suspend fun execute(command: List<String>) {
        execute(command.joinToString(" "))
    }
}