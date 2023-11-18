package com.imcys.bilibilias.okdownloader.internal.exception

internal open class TerminalException(override val message: String?) : RuntimeException(message)

internal class CancelException(override val message: String?) : TerminalException(message)
