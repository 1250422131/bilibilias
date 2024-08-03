package com.imcys.bilibilias.okdownloader

interface ErrorCode {
    companion object {
        const val SUCCESS: Int = 100
        const val APPEND_SUCCESS: Int = 101
        const val COPY_SUCCESS: Int = 102
        const val EXISTS_SUCCESS: Int = 103
        const val IO_CREATE_FILE_ERROR: Int = 201
        const val IO_CREATE_DIRECTORY_ERROR: Int = 202
        const val IO_STORAGE_FULL: Int = 203
        const val IO_INTERRUPTED: Int = 204
        const val IO_EXCEPTION: Int = 205
        const val NET_DISCONNECT: Int = 301
        const val NET_STREAM_RESET: Int = 302
        const val REMOTE_CONNECT_ERROR: Int = 401
        const val REMOTE_CONTENT_EMPTY: Int = 402
        const val VERIFY_FILE_NOT_EXISTS: Int = 501
        const val VERIFY_FILE_NOT_FILE: Int = 502
        const val VERIFY_MD5_NOT_MATCHED: Int = 503
        const val VERIFY_SIZE_NOT_MATCHED: Int = 504
        const val CANCEL: Int = 601
        const val PAUSE: Int = 602
        const val INTERRUPTED: Int = 603
        const val FILE_NOT_FOUND: Int = 606
        const val NETWORK_NOT_ALLOWED: Int = 701
        const val ARGUMENT_EXCEPTION: Int = 702
        const val MALFORMED_URL: Int = 703
        const val UNKNOWN: Int = -1
    }
}
