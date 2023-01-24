package com.imcys.bilibilias.tool_livestream.base

import android.os.Bundle
import com.imcys.bilibilias.common.base.AbsActivity

open class BaseActivity : AbsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式状态栏
        statusBarOnly(this)
    }
}