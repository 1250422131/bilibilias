package com.imcys.bilibilias.common.base.utils

import android.content.Context
import android.content.res.Configuration

fun isPad(context: Context): Boolean {
    val configuration: Configuration = context.resources.configuration
    val smallestScreenWidthDp: Int = configuration.smallestScreenWidthDp
    return smallestScreenWidthDp >= 600
}