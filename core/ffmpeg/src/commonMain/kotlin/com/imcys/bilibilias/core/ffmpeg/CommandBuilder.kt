package com.imcys.bilibilias.core.ffmpeg

class CommandBuilder internal constructor() {
    private val commands = mutableListOf<String>()
    fun add(command: String): CommandBuilder {
        commands.add(command)
        return this
    }

    fun build(): List<String> {
        return commands.toList()
    }
}

fun buildFfmpegCommand(block: CommandBuilder.() -> Unit): List<String> {
    return CommandBuilder().apply(block).build()
}