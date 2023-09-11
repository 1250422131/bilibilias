package com.imcys.bilibilias.common.base.utils

import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtils {

    fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() +
            recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }
}
