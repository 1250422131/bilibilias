package com.imcys.bilibilias.home.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.DialogUtils.setPad
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.data.entity.deepCopy
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.databinding.FragmentDownloadBinding
import com.imcys.bilibilias.home.ui.adapter.DownloadFinishTaskAd
import com.imcys.bilibilias.home.ui.adapter.DownloadTaskAdapter
import com.liulishuo.okdownload.OkDownloadProvider
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFragment : BaseFragment() {

    lateinit var fragmentDownloadBinding: FragmentDownloadBinding

    @Inject
    lateinit var downloadFinishTaskAd: DownloadFinishTaskAd

    @Inject
    lateinit var downloadTaskAdapter: DownloadTaskAdapter

    @Inject
    lateinit var downloadFinishTaskRepository: DownloadFinishTaskRepository

    @Inject
    lateinit var downloadQueue: DownloadQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DialogUtils.downloadQueue = downloadQueue
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentDownloadBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        // 添加边距
        fragmentDownloadBinding.fragmentDownloadTopLinearLayout.addStatusBarTopPadding()

        initView()


        return fragmentDownloadBinding.root
    }

    /**
     * 初始化布局
     */
    private fun initView() {
        initDownloadListAd()
        initEditLayout()
        initDownloadList()

        initTabLayout()
    }

    private fun initDownloadListAd() {
        downloadFinishTaskAd.mLongClickEvent = {
            fragmentDownloadBinding.run {
                if (fgDownloadTopEdit.visibility == View.VISIBLE) {
                    editCancel()
                    false
                } else {
                    editShow()
                    true
                }
            }
        }
    }

    private fun editCancel() {
        fragmentDownloadBinding.apply {
            fgDownloadTopEdit.visibility = View.GONE
            fgDownloadBottomEdit.visibility = View.GONE
            fragmentDownloadTabLayout.visibility = View.VISIBLE

            val newTaskList =
                downloadFinishTaskAd.currentList.map {
                    it.deepCopy {
                        selectState = false
                        showEdit = false
                    }
                }
            downloadFinishTaskAd.submitList(newTaskList)
        }
    }

    private fun editShow() {
        fragmentDownloadBinding.apply {
            fgDownloadTopEdit.visibility = View.VISIBLE
            fgDownloadBottomEdit.visibility = View.VISIBLE
            fragmentDownloadTabLayout.visibility = View.GONE
        }
    }

    private fun initEditLayout() {
        fragmentDownloadBinding.apply {
            // 全选
            fgDownloadEditSelectAll.setOnClickListener {
                val newTaskList =
                    downloadFinishTaskAd.currentList.map { it.deepCopy { selectState = true } }
                downloadFinishTaskAd.submitList(newTaskList)
            }
            // 反选
            fgDownloadEditInvert.setOnClickListener {
                val newTaskList =
                    downloadFinishTaskAd.currentList.map {
                        if (it.selectState) {
                            it.deepCopy { selectState = false }
                        } else {
                            it.deepCopy { selectState = true }
                        }
                    }
                downloadFinishTaskAd.submitList(newTaskList)
            }

            // 取消
            fgDownloadEditCancel.setOnClickListener {
                editCancel()
            }

            fgDownloadEditDelete.setOnClickListener {
                deleteFinishTaskTip()
            }
        }
    }

    private fun initTabLayout() {
        fragmentDownloadBinding.apply {
            fragmentDownloadTabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        when (tab.position) {
                            0 -> {
                                fragmentDownloadRecyclerView.adapter = downloadTaskAdapter
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
                },
            )
        }
    }

    /**
     * 弹出警告对话框
     */
    private fun deleteFinishTaskTip() {
        AsDialog.init(this).build {
            config = {
                title = "警告"
                content = "请选择删除方式？"
                positiveButtonText = "仅删除记录"
                neutralButtonText = "删除记录和文件"
                negativeButtonText = "取消"
                negativeButton = {
                    it.cancel()
                }
                neutralButton = {
                    deleteSelectTaskAndFile()
                    it.cancel()
                }
                positiveButton = {
                    deleteSelectTaskRecords()
                    it.cancel()
                }
            }
        }.setPad().show()
    }

    /**
     * 删除记录以及文件
     */
    private fun deleteSelectTaskAndFile() {
        downloadFinishTaskAd.currentList.filter { it.selectState }
            .forEach {task->
                val downloadFile = File(task.savePath)
                val uri = FileProvider.getUriForFile(
                    OkDownloadProvider.context,
                    "com.imcys.bilibilias.fileProvider",
                    downloadFile
                )
                try {
                    val deleted = OkDownloadProvider.context.contentResolver.delete(uri, null, null) > 0
                    if (deleted) {
                        asToast(OkDownloadProvider.context, "删除成功")
                    } else {
                        asToast(OkDownloadProvider.context, "删除失败")
                    }
                } catch ( e:Exception) {
                    asToast(OkDownloadProvider.context, "没有权限删除文件: ${e.message}")
                }
            }


        deleteSelectTaskRecords()
    }

    /**
     * 删除下载记录
     */
    private fun deleteSelectTaskRecords() {
        launchIO {
            downloadFinishTaskAd.currentList.filter { it.selectState }
                .forEach { downloadFinishTaskRepository.delete(it) }

            val newTasks = downloadFinishTaskRepository.allDownloadFinishTask()

            launchUI {
                editCancel()
                // 更新数据
                downloadFinishTaskAd.submitList(newTasks)
            }
        }
    }

    /**
     * 加载下载完成列表
     */
    private fun loadDownloadTask() {
        fragmentDownloadBinding.apply {
            fragmentDownloadRecyclerView.adapter = downloadFinishTaskAd
            launchUI {
                downloadFinishTaskAd.submitList(downloadFinishTaskRepository.allDownloadFinishTask())
            }
        }
    }

    /**
     * 下载列表
     */
    private fun initDownloadList() {
        fragmentDownloadBinding.apply {
            fragmentDownloadRecyclerView.apply {
                adapter = downloadTaskAdapter
                layoutManager = LinearLayoutManager(context)
                // 启用视图缓存
                setItemViewCacheSize(20)
                // 禁用动画以提高性能
                itemAnimator = null
                // 固定大小优化
                setHasFixedSize(true)
            }
        }
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            downloadQueue.downloadTasks
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .sample(200) // 增加采样间隔到200ms
                .distinctUntilChanged()
                .collect { tasks ->
                    // 使用 DiffUtil 进行增量更新
                    downloadTaskAdapter.submitList(tasks) {
                        // 更新完成后滚动到当前位置
//                        fragmentDownloadBinding.fragmentDownloadRecyclerView.scrollToPosition(
//                            (fragmentDownloadBinding.fragmentDownloadRecyclerView.layoutManager as LinearLayoutManager)
//                                .findFirstVisibleItemPosition()
//                        )
                    }
                }
        }

        // 已完成任务列表不需要频繁更新
        viewLifecycleOwner.lifecycleScope.launch {
            downloadQueue.finishedTasks
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .distinctUntilChanged()
                .collect { tasks ->
                    downloadFinishTaskAd.submitList(tasks)
                }
        }
    }

    companion object {
        fun newInstance() = DownloadFragment()
    }
}
