package com.imcys.network.download

import androidx.collection.*
import com.imcys.model.download.*

fun interface ProgressListener {
    fun set(task: ArrayMap<Task, Int>)
}
