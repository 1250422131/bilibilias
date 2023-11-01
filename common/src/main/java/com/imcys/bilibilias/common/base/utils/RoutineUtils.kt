package com.imcys.bilibilias.common.base.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun asLogD(context: Context, content: String) = Log.d(context::class.java.simpleName, content)

fun asToast(context: Context, content: String) =
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
