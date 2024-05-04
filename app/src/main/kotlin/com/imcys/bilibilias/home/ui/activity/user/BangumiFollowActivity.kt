package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.databinding.ActivityBangumiFollowBinding
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BangumiFollowActivity : BaseActivity<ActivityBangumiFollowBinding>() {
    override val layoutId: Int = R.layout.activity_bangumi_follow

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bangumiFollowTopLy.addStatusBarTopPadding()
    }

    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, BangumiFollowActivity::class.java)
            context.startActivity(intent)
        }
    }
}
