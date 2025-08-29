package com.imcys.bilibilias.core.datastore.model

enum class AudioQuality(val code: Int, val description: String) {
    ULTRA_HIGH_100010(100010, "100010"), // Or a more descriptive name if "100010" has a meaning
    ULTRA_HIGH_100009(100009, "100009"),
    ULTRA_HIGH_100008(100008, "100008"),
    HI_RES_LOSSLESS(30251, "Hi-Res无损"),
    DOLBY_ATMOS(30250, "杜比全景声"), // Assuming "杜比全景声" is Dolby Atmos
    BITRATE_192K(30280, "192K"),
    BITRATE_132K(30232, "132K"),
    BITRATE_64K(30216, "64K");

    companion object {
        fun fromCode(code: Int): AudioQuality? {
            return AudioQuality.entries.firstOrNull { it.code == code }
        }
    }
}
