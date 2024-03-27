package com.imcys.bilibilias.home.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hjq.toast.Toaster
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.getAppNotices
import com.imcys.bilibilias.base.utils.getDefaultSharedPreferences
import com.imcys.bilibilias.base.utils.setAppNotices
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.core.model.bilibilias.Banner
import com.imcys.bilibilias.core.model.bilibilias.UpdateNotice
import com.imcys.bilibilias.databinding.FragmentHomeBinding
import com.imcys.bilibilias.home.ui.adapter.OldHomeBeanAdapter
import com.imcys.bilibilias.home.ui.viewmodel.HomeViewModel
import com.imcys.bilibilias.home.ui.viewmodel.home.ApkVerify
import com.imcys.bilibilias.home.ui.viewmodel.home.HomeIntent
import com.xiaojinzi.component.anno.RouterAnno
import com.youth.banner.indicator.CircleIndicator
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@RouterAnno(
    hostAndPath = ARouterAddress.AppHomeFragment,
)
@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false,
        )
        fragmentHomeBinding.fragmentHomeTopLinearLayout.addStatusBarTopPadding()
        return fragmentHomeBinding.root
    }

    override fun initView() {
        fragmentHomeBinding.cardExitLogin.setOnClickListener {
            DialogUtils.dialog(
                requireContext(),
                "退出登录",
                "确定要退出登录了吗？",
                "是的",
                "点错了",
                true,
                positiveButtonClickListener = {
                    viewModel.exitAccountLogin()
                    Toaster.show("清除完成，请关闭后台重新进入")
                },
                negativeButtonClickListener = {},
            ).show()
        }
    }

    override fun initData() {
        viewModel.getUpdateNotice()
        viewModel.getBanner()
        startStatistics()
    }

    override fun initObserveViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.homeUiState.collect {
                    when (it) {
                        HomeIntent.Loading -> Unit
                        is HomeIntent.UpdateNoticeIntent -> handleBiliBiliAsUpdateData(it.updateNotice)
                        is HomeIntent.BannerIntent -> handleBannerData(it.banner)
                    }
                }
            }
        }
    }

    /**
     * 加载轮播图信息
     */
    private fun handleBannerData(banner: Banner) {
        fragmentHomeBinding.fragmentHomeBanner.setAdapter(OldHomeBeanAdapter(banner))
            .setIndicator(CircleIndicator(requireContext()))
    }

    private fun popUpVersionInformation(
        url: String,
        version: String,
        notices: String,
        id: String
    ) {
        if (BiliBiliAsApi.version.toString() != version) {
            DialogUtils.dialog(
                requireContext(),
                getString(R.string.app_HomeFragment_loadVersionData_title),
                notices,
                getString(R.string.app_HomeFragment_loadVersionData_positiveButtonText),
                getString(R.string.app_HomeFragment_loadVersionData_negativeButtonText),
                id != "3",
                positiveButtonClickListener = {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    requireContext().startActivity(intent)
                },
                negativeButtonClickListener = {
//                    val uri = Uri.parse(oldUpdateDataBean.url)
//                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                    requireContext().startActivity(intent)
                },
            ).show()
        }
    }

    /**
     * 加载公告
     */
    private fun handleBiliBiliAsUpdateData(updateNotice: UpdateNotice) {
//        AppCenter.start(requireActivity().application, App.appSecret, Distribute::class.java)

        postAndCheckSignatureMessage(updateNotice)
        // 检测更新
        popUpVersionInformation(
            updateNotice.url,
            updateNotice.version,
            updateNotice.gxNotice,
            updateNotice.id
        )

        popUpAppNotices(updateNotice.notice)
    }

    private fun popUpAppNotices(notice: String) {
        val notices = requireContext().getAppNotices()
        if (notices == notice) return

        DialogUtils.dialog(
            requireContext(),
            getString(R.string.app_HomeFragment_loadNotice_title),
            notice,
            getString(R.string.app_HomeFragment_loadNotice_positiveButtonText),
            getString(R.string.app_HomeFragment_loadNotice_negativeButtonText),
            true,
            positiveButtonClickListener = { requireContext().setAppNotices(notice) },
            negativeButtonClickListener = {},
        ).show()
    }

    /**
     * 送出此版本的数据信息
     */
    private fun postAndCheckSignatureMessage(notice: UpdateNotice) {
        val apkPath = requireContext().packageCodePath
        val sha = ApkVerify.apkVerifyWithSHA(apkPath)
        val md5 = ApkVerify.apkVerifyWithMD5(apkPath)
        val crc = ApkVerify.apkVerifyWithCRC(apkPath)

        when (notice.id) {
            "0" -> viewModel.postSignatureMessage(sha, md5, crc)
            "1" -> TODO()
        }
    }

    /**
     * 启动统计
     */
    internal fun startStatistics() {
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
