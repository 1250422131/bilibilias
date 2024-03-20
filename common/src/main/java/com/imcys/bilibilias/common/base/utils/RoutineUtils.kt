package com.imcys.bilibilias.base.utils

import android.content.Context
import android.widget.Toast

fun asToast(context: Context, content: String) =
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()