package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.baidu.mobstat.StatService
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.databinding.FragmentUserBinding
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragment() {

    lateinit var fragmentUserBinding: FragmentUserBinding

    override fun onResume() {
        super.onResume()
        StatService.onPageStart(context, "UserFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentUserBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)

        fragmentUserBinding.fragmentUserTopLinearLayout.addStatusBarTopPadding()

        return fragmentUserBinding.root
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, "UserFragment")
    }

    companion object {

        @JvmStatic
        fun newInstance() = UserFragment()
    }
}
