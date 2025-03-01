package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable


@Serializable
data class OldHomeFeedbackConfigBean(
    val icon: String,
    val title: String,
    val content: String,
    val url: String, )
