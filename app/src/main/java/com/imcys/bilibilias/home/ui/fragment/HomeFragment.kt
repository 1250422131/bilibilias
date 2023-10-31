package com.imcys.bilibilias.home.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.mobstat.StatService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hyy.highlightpro.HighlightPro
import com.hyy.highlightpro.parameter.Constraints
import com.hyy.highlightpro.parameter.HighlightParameter
import com.hyy.highlightpro.parameter.MarginOffset
import com.hyy.highlightpro.shape.RectShape
import com.hyy.highlightpro.util.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.extend.toColorInt
import com.imcys.bilibilias.common.base.model.OldUpdateDataBean
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.model.AuthQrCode
import com.imcys.bilibilias.common.base.utils.asToast
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.databinding.FragmentHomeBinding
import com.imcys.bilibilias.databinding.TipAppBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.adapter.OldHomeAdAdapter
import com.imcys.bilibilias.home.ui.adapter.OldHomeBeanAdapter
import com.imcys.bilibilias.home.ui.model.OldHomeAdBean
import com.imcys.bilibilias.home.ui.viewmodel.HomeViewModel
import com.imcys.bilibilias.view.base.BaseFragment
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.distribute.Distribute
import com.xiaojinzi.component.anno.RouterAnno
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import java.net.URLEncoder

@RouterAnno(
    hostAndPath = ARouterAddress.AppHomeFragment,
)
class HomeFragment : BaseFragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    internal lateinit var loginQRDialog: BottomSheetDialog

    // 懒加载
    private val bottomSheetDialog by lazy {
        DialogUtils.loadDialog(requireContext())
    }

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

        // 添加边距
        fragmentHomeBinding.apply {
            fragmentHomeTopLinearLayout.addStatusBarTopPadding()
            fragmentHomeViewModel =
                ViewModelProvider(this@HomeFragment)[HomeViewModel::class.java]
        }

        detectUserLogin()
        loadServiceData()
        return fragmentHomeBinding.root
    }

    override fun onResume() {
        super.onResume()
        // 判断用户是否没有被引导
        val guideVersion =
            (context as HomeActivity).asSharedPreferences.getString("AppGuideVersion", "")
        if (guideVersion != App.AppGuideVersion) {
            loadHomeGuide()
        }
        StatService.onPageStart(context, "HomeFragment")
    }

    /**
     * 加载引导
     */
    private fun loadHomeGuide() {
        HighlightPro.with(this)
            .setHighlightParameter {
                val tipAppBinding = TipAppBinding.inflate(LayoutInflater.from(context))
                tipAppBinding.tipAppTitle.text = getString(R.string.app_guide_home)
                HighlightParameter.Builder()
                    .setTipsView(tipAppBinding.root)
                    .setHighlightViewId(fragmentHomeBinding.fragmentHomeNewVersionLy.id)
                    .setHighlightShape(RectShape(4f.dp, 4f.dp, 6f))
                    .setHighlightHorizontalPadding(8f.dp)
                    .setConstraints(Constraints.TopToBottomOfHighlight + Constraints.EndToEndOfHighlight)
                    .setMarginOffset(MarginOffset(start = 8.dp))
                    .build()
            }
            .setBackgroundColor("#80000000".toColorInt())
            .setOnDismissCallback {
                // 让ViewPage来切换页面
                (activity as HomeActivity).binding.homeViewPage.currentItem = 1
                (activity as HomeActivity).binding.homeBottomNavigationView.menu.getItem(
                    1,
                ).isCheckable = true
            }
            .show()
    }

    /**
     * 加载服务器信息
     */
    private fun loadServiceData() {
        loadAppData()
        loadBannerData()
        initHomeAd()
    }

    /**
     * 加载首页广告
     */
    private fun initHomeAd() {
        val userGoogleADSwitch =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getBoolean("user_google_ad_switch", true)

        if (userGoogleADSwitch) {
            launchIO {
                val oldHomeAdBean = getOldHomeAdBean()
                // 切换主线程
                launchUI {
                    val adapter = OldHomeAdAdapter()
                    fragmentHomeBinding.fragmentHomeAdRv.adapter = adapter
                    fragmentHomeBinding.fragmentHomeAdRv.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                    adapter.submitList(oldHomeAdBean.data)
                }
            }
        }
    }

    /**
     * 获取广告请求
     * @return OldHomeAdBean
     */
    private suspend fun getOldHomeAdBean(): OldHomeAdBean {
        return HttpUtils.asyncGet(
            "${BiliBiliAsApi.appFunction}?type=oldHomeAd",
            OldHomeAdBean::class.java,
        )
    }

    /**
     * 加载轮播图信息
     */
    private fun loadBannerData() {
        OldHomeBeanAdapter(TODO(), TODO())
    }

    /**
     * 加载APP数据
     */
    private fun loadAppData() {
        AppCenter.start((context as Activity).application, App.appSecret, Distribute::class.java)
        loadNotice(TODO())
        // 检测更新
        loadVersionData(TODO())
    }

    /**
     * 加载版本信息
     */
    private fun loadVersionData(oldUpdateDataBean: OldUpdateDataBean) {
        if (BiliBiliAsApi.version.toString() != oldUpdateDataBean.version) {
            DialogUtils.dialog(
                requireContext(),
                getString(R.string.app_HomeFragment_loadVersionData_title),
                oldUpdateDataBean.gxnotice,
                getString(R.string.app_HomeFragment_loadVersionData_positiveButtonText),
                getString(R.string.app_HomeFragment_loadVersionData_negativeButtonText),
                oldUpdateDataBean.id != "3",
                positiveButtonClickListener = {
                    val uri = Uri.parse(oldUpdateDataBean.url)
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
     * @param notice String
     */
    private fun loadNotice(notice: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val mNotice = sharedPreferences.getString("AppNotice", "")
        if (mNotice != notice) {
            DialogUtils.dialog(
                requireContext(),
                getString(R.string.app_HomeFragment_loadNotice_title),
                notice,
                getString(R.string.app_HomeFragment_loadNotice_positiveButtonText),
                getString(R.string.app_HomeFragment_loadNotice_negativeButtonText),
                true,
                positiveButtonClickListener = {
                    sharedPreferences.edit().putString("AppNotice", notice).apply()
                },
                negativeButtonClickListener = {
                },
            ).show()
        }
    }

    @Deprecated("")
    /**
     * 加载登陆对话框
     */
    internal fun loadLogin() {
        HttpUtils.get(BilibiliApi.getLoginQRPath, AuthQrCode::class.java) {
            URLEncoder.encode(it.url, "UTF-8")
            loginQRDialog = DialogUtils.loginQRDialog(
                context as Activity,
                it,
            ) { code: Int, _: LoginStateBean ->
                // 登陆成功
                if (code == 0) {
                    initUserData()
                    startStatistics()
                }
            }.apply {
                show()
            }
        }
    }

    /**
     * 启动统计
     */
    internal fun startStatistics() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.edit()
            .putBoolean("microsoft_app_center_type", true)
            .putBoolean("baidu_statistics_type", true)
            .apply()
    }

    // 初始化用户数据
    internal fun initUserData() {
        // mid
        bottomSheetDialog.show()

        launchIO {
            val myUserData =
                KtHttpUtils.addHeader(COOKIE, asUser.cookie)
                    .asyncGet<MyUserData>(BilibiliApi.getMyUserData)

            launchUI {
                if (myUserData.mid == 0L) {
                    // 提交
                    BaseApplication.myUserData = myUserData
                } else {
                    asToast(requireContext(), "登录出现意外，请重新完成登录")
                    loadLogin()
                }

                bottomSheetDialog.cancel()
            }
        }
    }

    /**
     * 检查用户是否登陆
     */
    private fun detectUserLogin() {
        launchIO {
            val myUserData =
                HttpUtils.addHeader(COOKIE, BaseApplication.dataKv.decodeString(COOKIES, "")!!)
                    .asyncGet(
                        BilibiliApi.getMyUserData,
                        MyUserData::class.java,
                    )

            launchUI {
                if (myUserData.mid == 0L) {
                    DialogUtils.loginDialog(requireContext())
                        .show()
                } else {
                    BaseApplication.myUserData = myUserData
                }
            }
        }
    }

    // 加载用户数据

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(requireContext(), "HomeFragment")
    }
}
