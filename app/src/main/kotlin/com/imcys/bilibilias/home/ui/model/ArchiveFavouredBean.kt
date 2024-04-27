package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author:imcys
 * @create: 2023-01-03 16:23
 * @Description: 收藏检验
 */
@Serializable
data class ArchiveFavouredBean(
    @SerialName("count")
    val count: Int,
    @SerialName("favoured")
    var isFavoured: Boolean
):java.io.Serializable
