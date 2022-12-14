package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.databinding.FragmentDownloadBinding
import com.imcys.bilibilias.home.ui.adapter.DownloadTaskAdapter
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

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


    }

    /**
     * 下载列表
     */
    private fun initDownloadList() {
        fragmentDownloadBinding.apply {
            App.downloadQueue.recyclerView = fragmentDownloadRecyclerView

            fragmentDownloadRecyclerView.adapter = DownloadTaskAdapter()

            fragmentDownloadRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = DownloadFragment()
    }
}