package com.imcys.bilibilias.ui.analysis

import com.imcys.bilibilias.common.utils.TextType
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType

data class AnalysisUIState(
    val inputAsText: String = "",
    val linkType: TextType? = null,
    val asLinkResultType: ASLinkResultType? = null,
    val isBILILogin: Boolean = false,
    val downloadInfo: DownloadViewInfo? = null,
    val isCreateDownloadLoading: Boolean = false,
    val analysisBaseInfo: AnalysisBaseInfo = AnalysisBaseInfo(),
    val isSelectSingleModel: Boolean = true
)