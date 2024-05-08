package com.imcys.bilibilias.home.ui.activity

import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.databinding.ActivityDonateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonateActivity : BaseActivity<ActivityDonateBinding>() {
    override val layoutId: Int = R.layout.activity_donate

    override fun initView() {
    }

    companion object {
        const val PAY_XML = 1
        const val PAY_PROGRESS = 2
        const val PAY_DOC = 3
        const val PAY_TIP = 4
    }
}
