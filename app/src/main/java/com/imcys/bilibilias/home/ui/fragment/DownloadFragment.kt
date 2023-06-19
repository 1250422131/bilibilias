package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.data.AppDatabase
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.databinding.FragmentDownloadBinding
import com.imcys.bilibilias.home.ui.adapter.DownloadFinishTaskAd
import com.imcys.bilibilias.home.ui.adapter.DownloadTaskAdapter
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFragment : BaseFragment() {


    lateinit var fragmentDownloadBinding: FragmentDownloadBinding

    @Inject
    lateinit var downloadFinishTaskAd: DownloadFinishTaskAd

    @Inject
    lateinit var downloadTaskAdapter: DownloadTaskAdapter

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

    private fun initTabLayout() {
        fragmentDownloadBinding.apply {
            fragmentDownloadTabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        when (tab.position) {
                            0 -> {
                                fragmentDownloadRecyclerView.adapter =
                                    App.downloadQueue.downloadTaskAdapter
                            }

                            1 -> {
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
    private fun loadDownloadTask() {

        fragmentDownloadBinding.apply {
            App.downloadQueue.downloadFinishTaskAd = downloadFinishTaskAd
            fragmentDownloadRecyclerView.adapter = downloadFinishTaskAd


            lifecycleScope.launch(Dispatchers.IO) {
                val downloadFinishTaskDao =
                    AppDatabase.getDatabase(App.context.applicationContext).downloadFinishTaskDao()

                //协程提交
                DownloadFinishTaskRepository(downloadFinishTaskDao).apply {

                    App.downloadQueue.downloadFinishTaskAd?.apply {
                        val finishTasks = allDownloadFinishTask

                        lifecycleScope.launch(Dispatchers.Main) {
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
            App.downloadQueue.downloadTaskAdapter = downloadTaskAdapter

            fragmentDownloadRecyclerView.adapter = App.downloadQueue.downloadTaskAdapter
//            fragmentDownloadRecyclerView.itemAnimator = null
            fragmentDownloadRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = DownloadFragment()
    }
}