package com.imcys.bilibilias.home.ui.fragment.tool

import android.content.Context
import android.content.Intent
import com.imcys.bilibilias.core.model.OldToolItemBean
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import com.imcys.bilibilias.home.ui.activity.tool.MergeVideoActivity
import com.imcys.bilibilias.tool_log_export.ui.activity.LogExportActivity

data class ToolItem(
    val color: String,
    val imageUrl: String,
    val title: String,
    val toolCode: Int,
    val type: Int,
    val event: (Context) -> Unit = {}
) {
    init {
        require(type == VIEW_TYPE_TOOL || type == VIEW_TYPE_VIEW)
    }

    companion object {
        const val VIEW_TYPE_TOOL = 0
        const val VIEW_TYPE_VIEW = 1
    }
}

fun OldToolItemBean.mapToToolItems(): List<ToolItem> {
    return result.map {
        ToolItem(it.color, it.imageUrl, it.title, it.toolCode, ToolItem.VIEW_TYPE_TOOL)
    }
}

fun List<ToolItem>.setEvent(): List<ToolItem> {
    val result = mutableListOf<ToolItem>()
    result.add(this[0])
    result.add(
        this[1].copy(event = { it.startActivity(Intent(it, SettingActivity::class.java)) })
    )
    result.add(
        this[2].copy(event = { LogExportActivity.actionStart(it) })
    )
    result.add(
        this[3].copy(event = { MergeVideoActivity.actionStart(it) })
    )
    return result
}
