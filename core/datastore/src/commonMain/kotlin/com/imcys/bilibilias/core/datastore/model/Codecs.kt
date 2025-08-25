package com.imcys.bilibilias.core.datastore.model

enum class Codecs {
    DVH1,
    AV1,
    HEVC,
    AVC;

    companion object {
        fun tryParse(value: String): Codecs {
            return Codecs.entries.firstOrNull {
                value.startsWith(it.name, true)
            } ?: AVC
        }
    }
}