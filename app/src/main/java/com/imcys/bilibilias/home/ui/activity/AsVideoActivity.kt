package com.imcys.bilibilias.home.ui.activity

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import com.imcys.bilibilias.home.ui.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AsVideoActivity : ComponentActivity() {
    companion object {

        fun actionStart(context: Context, bvId: String) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", bvId)
            context.startActivity(intent)
        }

        @Deprecated("B站已经在弱化aid的使用，我们不确定这是否会被弃用，因此这个方法将无法确定时效性")
        fun actionStart(context: Context, aid: Long) {
        }
    }
}

