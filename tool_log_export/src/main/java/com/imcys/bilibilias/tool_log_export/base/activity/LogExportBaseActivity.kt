package com.imcys.bilibilias.tool_log_export.base.activity

import android.os.Bundle
import com.imcys.bilibilias.common.base.AbsActivity

open class LogExportBaseActivity : AbsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 沉浸式
        statusBarOnly(this)
    }
}
