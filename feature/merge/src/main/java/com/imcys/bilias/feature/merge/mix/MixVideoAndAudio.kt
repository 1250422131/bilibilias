package com.imcys.bilias.feature.merge.mix

import java.io.File

data class MixVideoAndAudio(
    val video: File,
    val audio: File,
    val mix: File,
    val title: String,
    var complete: Boolean = false
)
