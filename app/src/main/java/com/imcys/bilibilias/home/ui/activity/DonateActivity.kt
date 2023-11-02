package com.imcys.bilibilias.home.ui.activity

import android.os.Bundle
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ActivityDonateBinding
import com.imcys.bilibilias.home.ui.model.DonateViewBean
import com.imcys.bilibilias.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonateActivity : BaseActivity<ActivityDonateBinding>() {
    private val donateMutableList = mutableListOf<DonateViewBean>()

    override fun getLayoutRes(): Int = R.layout.activity_donate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
    }

    override fun initData() {
            val newMutableList = mutableListOf<DonateViewBean>()
        //     donateMutableList.add(
        //         DonateViewBean(PAY_XML)
        //     )
        //     newMutableList.add(
        //         DonateViewBean(PAY_PROGRESS, it)
        //     )
        //     newMutableList.add(
        //         DonateViewBean(PAY_DOC)
        //     )
        //     newMutableList.add(
        //         DonateViewBean(
        //             PAY_TIP,
        //             tipBean = TipBean(
        //                 "为其他参与者捐款",
        //                 "现在你有更多选择",
        //                 "你不仅仅可以给服务器捐款，你还可以去看看参与贡献的人员，你对哪方面感到有价值，那么你就可以选择为其应援。",
        //                 AppCompatResources.getDrawable(this, R.drawable.ic_home_red_envelopes)!!,
        //                 "",
        //                 DedicateActivity::class.java
        //             )
        //         )
        //     )
        // }
    }

    companion object {
        const val PAY_XML = 1
        const val PAY_PROGRESS = 2
        const val PAY_DOC = 3
        const val PAY_TIP = 4
    }
}
