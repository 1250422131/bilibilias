package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// TODO: 添加头像框
@OptIn(ExperimentalUuidApi::class)
@Serializable
data class SelfInfo(
    val id: Uuid,
    val mid: Long,
    val nickname: String,
    val avatarUrl: String,
)
