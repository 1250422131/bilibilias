package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.utils.file.FileUtils
import com.imcys.bilibilias.common.data.entity.deepCopy
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.databinding.FragmentDownloadBinding
import com.imcys.bilibilias.home.ui.adapter.DownloadFinishTaskAd
import com.imcys.bilibilias.home.ui.adapter.DownloadTaskAdapter
import com.imcys.bilibilias.view.base.BaseFragment
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentDownloadBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        fragmentDownloadBinding.fragmentDownloadTopLinearLayout.addStatusBarTopPadding()
        return fragmentDownloadBinding.root
    }

    /**
     * 初始化布局
     */
    override fun initView() {
        initDownloadListAd()
        initEditLayout()
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
        }.show()
    }

    /**
     * 删除记录以及文件
     */
    private fun deleteSelectTaskAndFile() {
        downloadFinishTaskAd.currentList.filter { it.selectState }
            .forEach { FileUtils.deleteFile(it.savePath) }
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
        launchIO {
            downloadFinishTaskRepository.allDownloadFinishTask()
        }
    }

    companion object {
        fun newInstance() = DownloadFragment()
    }
}
