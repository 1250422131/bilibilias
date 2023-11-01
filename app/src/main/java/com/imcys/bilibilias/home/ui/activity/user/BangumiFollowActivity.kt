package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.ActivityBangumiFollowBinding
import com.imcys.bilibilias.view.base.BaseActivity
import com.imcys.model.BangumiFollowList
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BangumiFollowActivity : BaseActivity<ActivityBangumiFollowBinding>() {

    // private val bangumiFollowMutableList = mutableListOf<BangumiFollowList.DataBean.ListBean>()
    private lateinit var bangumiFollowList: com.imcys.model.BangumiFollowList
    override fun getLayoutRes(): Int = R.layout.activity_bangumi_follow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bangumiFollowTopLy.addStatusBarTopPadding()
    }

    override fun initView() {
        binding.apply {
            bangumiFollowRv.layoutManager = LinearLayoutManager(this@BangumiFollowActivity)

            HttpUtils.addHeader("coolie", asUser.cookie).get(
                "${BilibiliApi.bangumiFollowPath}?vmid=${asUser.mid}&type=1&pn=1&ps=15",
                com.imcys.model.BangumiFollowList::class.java
            ) {

            }

            bangumiFollowRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun loadBangumiFollow(pn: Int) {
        HttpUtils.get(
            "${BilibiliApi.bangumiFollowPath}?vmid=${asUser.mid}&type=1&pn=$pn&ps=15",
            com.imcys.model.BangumiFollowList::class.java
        ) {

        }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, BangumiFollowActivity::class.java)
            context.startActivity(intent)
        }
    }
}
