package com.imcys.bilibilias.core.domain.model

class Resolution(val id: Int, val displayName: String) {
    companion object {
        val R60A = Resolution(30216, "64K")
        val R132A = Resolution(30232, "132K")
        val R192A = Resolution(30280, "192K")
        val DolbyA = Resolution(30250, "杜比全景声")
        val HiRes = Resolution(30251, "Hi-Res无损")
        val R4320P = Resolution(127, "8K")
        val DolbyV = Resolution(126, "杜比视界")
        val HDR = Resolution(125, "HDR 真彩色")
        val R2160P = Resolution(120, "4K")
        val R1080P60 = Resolution(116, "1080P60 高帧率")
        val R1080PPlus = Resolution(112, "1080P 高码率")
        val AI = Resolution(100, "AI修复")
        val R1080P = Resolution(80, "1080P 高清")
        val R720P60 = Resolution(74, "720P60 高帧率")
        val R720P = Resolution(64, "720P 准高清")
        val R480P = Resolution(32, "480P 标清")
        val R360P = Resolution(16, "360P 流畅")
        fun values() = listOf(
            R60A,
            R132A,
            R192A,
            DolbyA,
            HiRes,
            R4320P,
            DolbyV,
            HDR,
            R2160P,
            R1080P60,
            R1080PPlus,
            AI,
            R1080P,
            R720P60,
            R720P,
            R480P,
            R360P,
        )
    }
}