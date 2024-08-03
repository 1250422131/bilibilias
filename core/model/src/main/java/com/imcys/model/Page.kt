package com.imcys.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class Page(
    @JsonNames("count", "total")
    val count: Int = 0,
    @JsonNames("pn", "num", "page_num")
    val pageNumber: Int = 0,
    @JsonNames("ps", "size", "page_size")
    val pageSize: Int = 0
)
