package com.imcys.bilibilias.core.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Payload(@SerialName("payload") val inner: String)
