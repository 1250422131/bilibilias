package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.model.common.BangumiFollowList
import com.imcys.bilibilias.common.base.utils.RecyclerViewUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.ActivityBangumiFollowBinding
import com.imcys.bilibilias.home.ui.adapter.BangumiFollowAdapter
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.ceil

@AndroidEntryPoint
class BangumiFollowActivity : BaseActivity() {

    lateinit var binding: ActivityBangumiFollowBinding

    @Inject
    lateinit var bangumiFollowAdapter: BangumiFollowAdapter

    @Inject
    lateinit var networkService: NetworkService
    private val bangumiFollowMutableList = mutableListOf<BangumiFollowList.DataBean.ListBean>()
    private lateinit var bangumiFollowList: BangumiFollowList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bangumi_follow)
        binding.apply {
            bangumiFollowTopLy.addStatusBarTopPadding()
        }

        initView()
    }

    private fun initView() {
        initRv()
    }

    private fun initRv() {
        launchUI {
            binding.apply {
                bangumiFollowRv.adapter = bangumiFollowAdapter
                bangumiFollowRv.layoutManager = LinearLayoutManager(this@BangumiFollowActivity)
                val mBangumiFollowList =
                    networkService.getBangumiFollow(asUser.mid, 1, 1, 15)

                if (mBangumiFollowList.code == 0) {
                    bangumiFollowList = mBangumiFollowList
                    bangumiFollowMutableList.addAll(mBangumiFollowList.data.list)
                    bangumiFollowAdapter.submitList(bangumiFollowMutableList + mutableListOf())
                }

                bangumiFollowRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (RecyclerViewUtils.isSlideToBottom(recyclerView)) {
                            if (ceil((bangumiFollowList.data.total / 15).toDouble()) > bangumiFollowList.data.pn + 1) {
                                loadBangumiFollow(bangumiFollowList.data.pn + 1)
                            }
                        }
                    }
                })
            }

        }
    }

    private fun loadBangumiFollow(pn: Int) {
        launchUI {
            val mBangumiFollowList =
                networkService.getBangumiFollow(asUser.mid, 1, pn, 15)
            if (mBangumiFollowList.code == 0) {
                bangumiFollowList = mBangumiFollowList
                bangumiFollowMutableList.addAll(mBangumiFollowList.data.list)
                bangumiFollowAdapter.submitList(bangumiFollowMutableList + mutableListOf())
            }
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
        fun actionStart(context: Context) {
            val intent = Intent(context, BangumiFollowActivity::class.java)
            context.startActivity(intent)
        }
    }
}
