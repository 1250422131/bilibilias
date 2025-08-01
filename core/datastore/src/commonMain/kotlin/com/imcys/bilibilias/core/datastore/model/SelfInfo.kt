package com.imcys.bilibilias.core.datastore.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class SelfInfo(
    val id: Uuid,
    val nickname: String,
    val avatarUrl: String,
)
