package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.FragmentDownloadBinding
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding

class DownloadFragment : Fragment() {


    lateinit var fragmentDownloadBinding: FragmentDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentDownloadBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false)
        //添加边距
        fragmentDownloadBinding.fragmentDownloadTopLinearLayout.addStatusBarTopPadding()
        return fragmentDownloadBinding.root

    }

    companion object {

        @JvmStatic
        fun newInstance() = DownloadFragment()
    }
}