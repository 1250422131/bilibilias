package com.imcys.bilibilias.home.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.utils.VideoUtils
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AsVideoActivity : BaseActivity<com.imcys.bilibilias.databinding.ActivityAsVideoBinding>() {

    private var videoID by mutableStateOf("")

    // 视频临时数据，方便及时调用，此方案考虑废弃
    var bvid: String = ""
    var cid: Long = 0L
    override fun getLayoutRes(): Int = R.layout.activity_as_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoID = intent.getStringExtra(BVID)!!
    }

    /**
     * 加载用户信息，为了确保会员视频及时通知用户
     */
    override fun initData() {
    }

    override fun initView() {

    }

    companion object {

        fun actionStart(context: Context, bvId: String) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", bvId)
            context.startActivity(intent)
        }

        @Deprecated("B站已经在弱化aid的使用，我们不确定这是否会被弃用，因此这个方法将无法确定时效性")
        fun actionStart(context: Context, aid: Long) {
            val intent = Intent(context, AsVideoActivity::class.java)
            intent.putExtra("bvId", VideoUtils.toBvidOffline(aid))
            context.startActivity(intent)
        }
    }
}

