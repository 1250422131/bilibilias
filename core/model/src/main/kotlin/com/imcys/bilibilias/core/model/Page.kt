package com.imcys.bilibilias.core.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Page(
    @JsonNames("count")
    val count: Int = 0,
    @JsonNames("pn")
    val pn: Int = 0,
    @JsonNames("ps")
    val ps: Int = 0
)
