package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.baidu.mobstat.StatService
import com.hyy.highlightpro.HighlightPro
import com.hyy.highlightpro.parameter.Constraints
import com.hyy.highlightpro.parameter.HighlightParameter
import com.hyy.highlightpro.parameter.MarginOffset
import com.hyy.highlightpro.shape.RectShape
import com.hyy.highlightpro.util.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.extend.launchUI
import com.imcys.bilibilias.common.base.extend.toColorInt
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.OldToolItemBean
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.databinding.FragmentToolBinding
import com.imcys.bilibilias.databinding.TipAppBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import com.imcys.bilibilias.home.ui.activity.tool.MergeVideoActivity
import com.imcys.bilibilias.home.ui.activity.tool.WebAsActivity
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.tool_log_export.ui.activity.LogExportActivity
import com.imcys.bilibilias.view.base.BaseFragment
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RouterAnno(
    hostAndPath = ARouterAddress.ToolFragment,
)
class ToolFragment : BaseFragment() {

    lateinit var fragmentToolBinding: FragmentToolBinding

    @SuppressLint("CommitPrefEdits")
    override fun onResume() {
        super.onResume()
        // 这里仍然是在判断是否有被引导过了
        val guideVersion =
            (context as HomeActivity).asSharedPreferences.getString("AppGuideVersion", "")
        if (guideVersion != App.AppGuideVersion) {
            (context as HomeActivity).asSharedPreferences.edit()
                .putString("AppGuideVersion", App.AppGuideVersion).apply()
            loadToolGuide()
        }
        StatService.onPageStart(context, "ToolFragment")
    }

    private fun loadToolGuide() {
        val tipAppBinding = TipAppBinding.inflate(LayoutInflater.from(context))
        HighlightPro.with(this)
            .setHighlightParameter {
                tipAppBinding.tipAppTitle.text = getString(R.string.app_guide_tool)
                HighlightParameter.Builder()
                    .setTipsView(tipAppBinding.root)
                    .setHighlightViewId(fragmentToolBinding.fragmentToolSearch.id)
                    .setHighlightShape(RectShape(4f.dp, 4f.dp, 6f))
                    .setHighlightHorizontalPadding(8f.dp)
                    .setConstraints(Constraints.BottomToTopOfHighlight + Constraints.EndToEndOfHighlight)
                    .setMarginOffset(MarginOffset(start = 8.dp))
                    .build()
            }
            .setOnDismissCallback {
                (activity as HomeActivity).binding.homeViewPage.currentItem = 0
                (activity as HomeActivity).binding.homeBottomNavigationView.menu.getItem(
                    0,
                ).isCheckable = true
            }
            .setBackgroundColor("#80000000".toColorInt())
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentToolBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tool, container, false)
        return fragmentToolBinding.root
    }
    override fun initView() {
        // 加载工具item
        loadToolItem()
    }

    /**
     * 分享检查
     * 如果外部有分享内容，就会在这里过滤
     * @param intent Intent?
     */
    @SuppressLint("ResourceType")
    internal fun parseShare(intent: Intent?) {
        val action = intent?.action
        val type = intent?.type
        lifecycleScope.launchWhenResumed {
            if (Intent.ACTION_SEND == action && type != null) {
                if ("text/plain" == type) {
                    asVideoId(intent.getStringExtra(Intent.EXTRA_TEXT).toString())
                }
            }
            // 下面这段代表是从浏览器解析过来的
            val asUrl = intent?.extras?.getString("asUrl")
            if (asUrl != null) {
                asVideoId(asUrl)
            }
        }
    }

    /**
     * 对输入的视频ID进行解析
     * @param inputString String
     */
    fun asVideoId(inputString: String) {
        // ep过滤
        val epRegex = Regex("""(?<=ep)([0-9]+)""")
        // 判断是否有搜到
        if (epRegex.containsMatchIn(inputString)) {
            loadEpVideoCard(epRegex.find(inputString)?.value!!.toLong())
        }
    }

    /**
     * 利用ep号进行检索
     * @param epId Int
     */
    private fun loadEpVideoCard(epId: Long) {
        lifecycleScope.launch(Dispatchers.Default) {
            KtHttpUtils.addHeader(COOKIE, asUser.cookie)
                .asyncGet<Bangumi>("${BilibiliApi.bangumiVideoDataPath}?ep_id=$epId")
        }
    }

    /**
     * 加载支持工具的item
     */
    private fun loadToolItem() {
        val toolItemMutableList = mutableListOf<ToolItemBean>()
        launchIO {
            // 通过远程数据获取item
            val oldToolItemBean = listOf<OldToolItemBean>()
            launchUI {
                oldToolItemBean.forEach {
                    when (it.toolCode) {
                        // 设置
                        2 -> {
                            val intent = Intent(context, SettingActivity::class.java)
                            requireActivity().startActivity(intent)
                        }
                        // web解析
                        3 -> {
                            val intent = Intent(context, WebAsActivity::class.java)
                            requireActivity().startActivity(intent)
                        }
                        // 导出日志
                        4 -> {
                            LogExportActivity.actionStart(requireContext())
                        }
                        // 独立合并
                        5 -> {
                            MergeVideoActivity.actionStart(requireContext())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, getString(R.string.app_ToolFragment_onDestroy))
    }

    companion object {
        @JvmStatic
        fun newInstance() = ToolFragment()
    }
}
