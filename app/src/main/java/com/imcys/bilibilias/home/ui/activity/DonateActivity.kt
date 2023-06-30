package com.imcys.bilibilias.home.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.ActivityDonateBinding
import com.imcys.bilibilias.home.ui.adapter.DonateItemAdapter
import com.imcys.bilibilias.home.ui.model.DonateViewBean
import com.imcys.bilibilias.home.ui.model.OldDonateBean
import com.imcys.bilibilias.home.ui.model.TipBean
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DonateActivity : BaseActivity() {
    private val donateMutableList = mutableListOf<DonateViewBean>()
    lateinit var binding: ActivityDonateBinding

    @Inject
    lateinit var donateAdapter: DonateItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_donate)
        initView()
    }


    private fun initView() {

        binding.apply {
            donateTopLy.addStatusBarTopPadding()
        }
        initRv()
        loadDonateData()
    }

    private fun initRv() {

        binding.apply {
            donateRv.adapter = donateAdapter
            donateRv.layoutManager =
                LinearLayoutManager(this@DonateActivity, LinearLayoutManager.VERTICAL, false)
        }


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadDonateData() {
        HttpUtils.get("${BiliBiliAsApi.appFunction}?type=Donate", OldDonateBean::class.java) {
            val newMutableList = mutableListOf<DonateViewBean>()
            donateMutableList.add(
                DonateViewBean(PAY_XML)
            )
            newMutableList.add(
                DonateViewBean(PAY_PROGRESS, it)
            )
            newMutableList.add(
                DonateViewBean(PAY_DOC)
            )
            newMutableList.add(
                DonateViewBean(
                    PAY_TIP,
                    tipBean = TipBean(
                        "为其他参与者捐款",
                        "现在你有更多选择",
                        "你不仅仅可以给服务器捐款，你还可以去看看参与贡献的人员，你对哪方面感到有价值，那么你就可以选择为其应援。",
                        getDrawable(R.drawable.ic_home_red_envelopes)!!,
                        "",
                        DedicateActivity::class.java
                    )
                )
            )

            donateAdapter.submitList(donateMutableList + newMutableList)
        }
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
        const val PAY_XML = 1
        const val PAY_PROGRESS = 2
        const val PAY_DOC = 3
        const val PAY_TIP = 4
    }
}