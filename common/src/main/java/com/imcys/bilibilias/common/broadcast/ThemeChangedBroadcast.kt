package com.imcys.bilibilias.common.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.imcys.bilibilias.common.base.AbsActivity

/**
 * 主题广播监听器
 */

class ThemeChangedBroadcast : BroadcastReceiver() {
    /**
     * 这里主要是监听主题改变的，转发通知
     * @param context Context
     * @param intent Intent
     */

    override fun onReceive(context: Context, intent: Intent) {
        (context as AbsActivity).updateTheme()
    }
}