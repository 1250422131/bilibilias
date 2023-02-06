package com.imcys.bilibilias.tool_log_export.data.model

import com.imcys.bilibilias.tool_log_export.data.mEnum.ExportItemEnum

data class ExportItemBean(
    var itemType: ExportItemEnum,
    var title: String,
    var longTitle: String,
    var content: String,
    var icoUrl: String,
)