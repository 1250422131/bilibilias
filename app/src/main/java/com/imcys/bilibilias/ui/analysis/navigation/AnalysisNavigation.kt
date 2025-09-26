package com.imcys.bilibilias.ui.analysis.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class AnalysisRoute(
    var asInputText: String = ""
): NavKey
