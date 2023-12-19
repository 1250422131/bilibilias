package com.imcys.bilias.feature.merge

import com.imcys.model.download.Entry

data class SelectedResource(
    val entry: Entry = Entry(),
    val selected: Boolean = false
)
