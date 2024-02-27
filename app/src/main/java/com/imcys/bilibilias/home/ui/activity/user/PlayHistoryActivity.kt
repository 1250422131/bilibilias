package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.common.base.utils.RecyclerViewUtils
import com.imcys.bilibilias.databinding.ActivityPlayHistoryBinding
import com.imcys.bilibilias.home.ui.adapter.PlayHistoryAdapter
import com.imcys.bilibilias.home.ui.model.PlayHistoryBean
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayHistoryActivity : BaseActivity() {
    private lateinit var binding: ActivityPlayHistoryBinding

    // 自动装配
    @Inject
    lateinit var playHistoryAdapter: PlayHistoryAdapter

    @Inject
    lateinit var networkService: NetworkService

    private var max = 0L
    private var viewAt = 0L

    private val playHistoryDataMutableList: MutableList<PlayHistoryBean.DataBean.ListBean> =
        mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityPlayHistoryBinding?>(
            this,
            R.layout.activity_play_history
        ).apply {
            playHistoryTopLy.addStatusBarTopPadding()
        }
        initView()
    }

    private fun initView() {
        initPlayHistory()
    }

    private fun initPlayHistory() {
        binding.apply {
            playHistoryTopRv.adapter = playHistoryAdapter
            playHistoryTopRv.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            launchUI {
                networkService.getPlayHistory(0, 0).let {
                    max = it.data.cursor.max
                    viewAt = it.data.cursor.view_at
                    playHistoryDataMutableList.addAll(it.data.list)
                    playHistoryAdapter.submitList(playHistoryDataMutableList + mutableListOf())
                }
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
        launchUI {
            networkService.getPlayHistory(max, viewAt).let {
                max = it.data.cursor.max
                viewAt = it.data.cursor.view_at
                playHistoryDataMutableList.addAll(it.data.list)
                playHistoryAdapter.submitList(playHistoryDataMutableList + mutableListOf())
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
            val intent = Intent(context, PlayHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}
