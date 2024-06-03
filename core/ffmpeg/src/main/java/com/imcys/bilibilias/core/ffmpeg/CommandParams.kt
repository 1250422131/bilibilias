package com.imcys.bilibilias.core.ffmpeg

fun buildCommandParams(builderAction: CommandParams.() -> Unit): Array<String> {
    return CommandParams().apply(builderAction).get()
}

class CommandParams {
    private val data = mutableListOf<String>()

    fun append(param: String) = apply {
        data.add(param)
    }

    fun append(param: Int) = apply {
        data.add(param.toString())
    }

    fun append(param: Long) = apply {
        data.add(param.toString())
    }

    fun remove(param: String) = apply {
        data.remove(param)
    }

    fun clear() = apply {
        data.clear()
    }

    fun get(): Array<String> {
        return data.toTypedArray()
    }

    override fun toString(): String {
        return data.joinToString(" ")
    }
}
