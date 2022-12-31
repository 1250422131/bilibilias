package com.imcys.bilibilias.utils

import androidx.recyclerview.widget.RecyclerView

object RecyclerViewUtils {

    fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }
}