package com.imcys.bilibilias.home.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.edit
import com.imcys.bilibilias.base.utils.getDefaultSharedPreferences
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.feature.home.navigation.HomeFragmentScreen
import com.imcys.bilibilias.home.ui.activity.DedicateActivity
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                HomeFragmentScreen(
                    onSalute = {
                        val intent = Intent(context, DedicateActivity::class.java)
                        context.startActivity(intent)
                    },
                    onDonation = {
                        val intent = Intent(context, DonateActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }

    override fun initView() = Unit

    override fun initData() {
        startStatistics()
    }

    override fun initObserveViewModel() = Unit

    /**
     * 启动统计
     */
    private fun startStatistics() {
        requireContext().getDefaultSharedPreferences().edit {
            putBoolean("microsoft_app_center_type", true)
            putBoolean("baidu_statistics_type", true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
