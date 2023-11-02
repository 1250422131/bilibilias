package com.imcys.bilibilias.home.ui.activity.user

import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ActivityPlayHistoryBinding
import com.imcys.bilibilias.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayHistoryActivity : BaseActivity<ActivityPlayHistoryBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_play_history
}
