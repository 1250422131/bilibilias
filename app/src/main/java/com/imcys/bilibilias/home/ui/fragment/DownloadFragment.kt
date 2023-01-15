package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.databinding.FragmentDownloadBinding
import com.imcys.bilibilias.home.ui.adapter.DownloadFinishTaskAd
import com.imcys.bilibilias.home.ui.adapter.DownloadTaskAdapter
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DownloadFragment : Fragment() {


    lateinit var fragmentDownloadBinding: FragmentDownloadBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        fragmentDownloadBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        //添加边距
        fragmentDownloadBinding.fragmentDownloadTopLinearLayout.addStatusBarTopPadding()


        initView()

        return fragmentDownloadBinding.root

    }

    /**
     * 初始化布局
     */
    private fun initView() {

        initDownloadList()

        initTabLayout()


    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initTabLayout() {
        fragmentDownloadBinding.apply {
            fragmentDownloadTabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        when (tab?.text) {
                            "正在下载" -> {
                                fragmentDownloadRecyclerView.adapter =
                                    App.downloadQueue.downloadTaskAdapter
                            }
                            "下载完成" -> {
                                loadDownloadTask()
                            }
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                }
            )
        }
    }

    /**
     * 加载下载完成列表
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun loadDownloadTask() {

        fragmentDownloadBinding.apply {
            App.downloadQueue.downloadFinishTaskAd = DownloadFinishTaskAd()
            fragmentDownloadRecyclerView.adapter = App.downloadQueue.downloadFinishTaskAd

            GlobalScope.launch(Dispatchers.IO) {
                val downloadFinishTaskDao =
                    AppDatabase.getDatabase(App.context.applicationContext).downloadFinishTaskDao()

                //协程提交
                DownloadFinishTaskRepository(downloadFinishTaskDao).apply {

                    App.downloadQueue.downloadFinishTaskAd?.apply {
                        val finishTasks = allDownloadFinishTask
                        GlobalScope.launch(Dispatchers.Main) {
                            submitList(finishTasks)
                        }
                    }

                }
            }

        }

    }

    /**
     * 下载列表
     */
    private fun initDownloadList() {

        fragmentDownloadBinding.apply {
            App.downloadQueue.downloadTaskAdapter = DownloadTaskAdapter()
            fragmentDownloadRecyclerView.adapter = App.downloadQueue.downloadTaskAdapter
            fragmentDownloadRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = DownloadFragment()
    }
}