package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.databinding.FragmentToolBinding
import com.imcys.bilibilias.home.ui.adapter.ViewHolder
import com.imcys.bilibilias.home.ui.model.*
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ToolFragment : BaseFragment() {

    private lateinit var fragmentToolBinding: FragmentToolBinding
    private lateinit var mRecyclerView: RecyclerView
    private var isInitialized = false
    private var sharedIntent: Intent? = null

    lateinit var mAdapter: ListAdapter<ToolItemBean, ViewHolder>

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var downloadQueue: DownloadQueue

    @SuppressLint("CommitPrefEdits")
    override fun onResume() {
        super.onResume()
        StatService.onPageStart(context, "ToolFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentToolBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tool, container, false)
        return fragmentToolBinding.root
    }

    override fun initView() {
        DialogUtils.downloadQueue = downloadQueue

        // 设置布局不浸入
        fragmentToolBinding.fragmentToolTopLy.addStatusBarTopPadding()

        // 绑定列表
        mRecyclerView = fragmentToolBinding.fragmentToolRecyclerView
    }

    override fun initData() {
    }

    /**
     * 分享检查
     * 如果外部有分享内容，就会在这里过滤
     * @param intent Intent?
     */
    @SuppressLint("ResourceType")
    internal fun parseShare(intent: Intent?) {
        val action = intent?.action
        val type = intent?.type

        // 下面这段代表是从浏览器解析过来的
        val asUrl = intent?.extras?.getString("asUrl")
        if (asUrl != null) {
        }

        if (isInitialized) {
            if (Intent.ACTION_SEND == action && type != null) {
                if ("text/plain" == type) {
                }
            }
        } else {
            sharedIntent = intent
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ToolFragment()
    }
}
