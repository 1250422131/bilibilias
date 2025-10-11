package com.imcys.bilibilias.ui.download.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
data class DownloadRoute(
    val defaultListIndex: Int = 0,
): NavKey

