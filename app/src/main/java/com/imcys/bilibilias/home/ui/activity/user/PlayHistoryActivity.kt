package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.utils.RecyclerViewUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.ActivityPlayHistoryBinding
import com.imcys.bilibilias.home.ui.model.PlayHistoryBean
import com.imcys.bilibilias.view.base.BaseActivity
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayHistoryActivity : BaseActivity<ActivityPlayHistoryBinding>() {

    private var max = 0L
    private var viewAt = 0L

    private val playHistoryDataMutableList: MutableList<PlayHistoryBean.DataBean.ListBean> =
        mutableListOf()

    override fun getLayoutRes(): Int = R.layout.activity_play_history

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.playHistoryTopLy.addStatusBarTopPadding()
    }

    override fun initView() {
        binding.apply {
            playHistoryTopRv.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            HttpUtils.addHeader(COOKIE, asUser.cookie)
                .get(
                    "${BilibiliApi.userPlayHistoryPath}?max=0&view_at=0&type=archive",
                    PlayHistoryBean::class.java
                ) {
                    max = it.data.cursor.max
                    viewAt = it.data.cursor.view_at
                    playHistoryDataMutableList.addAll(it.data.list)
                }

            playHistoryTopRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (RecyclerViewUtils.isSlideToBottom(recyclerView)) {
                        loadPlayHistory()
                    }
                }
            })
        }
    }

    private fun loadPlayHistory() {
        HttpUtils.addHeader(COOKIE, asUser.cookie)
            .get(
                "${BilibiliApi.userPlayHistoryPath}?max=$max&view_at=$viewAt&type=archive",
                PlayHistoryBean::class.java
            ) {
                max = it.data.cursor.max
                viewAt = it.data.cursor.view_at
                playHistoryDataMutableList.addAll(it.data.list)
            }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, PlayHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
