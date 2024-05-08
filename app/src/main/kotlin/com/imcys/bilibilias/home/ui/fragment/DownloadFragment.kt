package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.imcys.asbottomdialog.bottomdialog.AsDialog
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.data.repository.DownloadFinishTaskRepository
import com.imcys.bilibilias.databinding.FragmentDownloadBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFragment : BaseFragment() {

    lateinit var fragmentDownloadBinding: FragmentDownloadBinding

    @Inject
    lateinit var downloadFinishTaskRepository: DownloadFinishTaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentDownloadBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        // 添加边距
        fragmentDownloadBinding.fragmentDownloadTopLinearLayout

        initView()

        return fragmentDownloadBinding.root
    }

    /**
     * 初始化布局
     */
    override fun initView() {
        initDownloadListAd()
        initEditLayout()
        initDownloadList()

        initTabLayout()
    }

    override fun initData() {
    }

    private fun initDownloadListAd() {
    }

    private fun editCancel() {
        fragmentDownloadBinding.apply {
            fgDownloadTopEdit.visibility = View.GONE
            fgDownloadBottomEdit.visibility = View.GONE
            fragmentDownloadTabLayout.visibility = View.VISIBLE
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
            }
            // 反选
            fgDownloadEditInvert.setOnClickListener {
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
        deleteSelectTaskRecords()
    }

    /**
     * 删除下载记录
     */
    private fun deleteSelectTaskRecords() {
    }

    /**
     * 加载下载完成列表
     */
    private fun loadDownloadTask() {
    }

    /**
     * 下载列表
     */
    private fun initDownloadList() {
    }

    companion object {
        fun newInstance() = DownloadFragment()
    }
}
