package com.imcys.bilias.feature.merge.ass

@OptIn(ExperimentalStdlibApi::class)
internal val colorFormat = HexFormat {
    number.prefix = "&H"
    number.removeLeadingZeros = true
}