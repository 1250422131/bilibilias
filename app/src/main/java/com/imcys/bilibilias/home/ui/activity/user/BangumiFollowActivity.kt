package com.imcys.bilibilias.home.ui.activity.user

import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ActivityBangumiFollowBinding
import com.imcys.bilibilias.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BangumiFollowActivity : BaseActivity<ActivityBangumiFollowBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_bangumi_follow
}
