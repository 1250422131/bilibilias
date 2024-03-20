package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.hyy.highlightpro.HighlightPro
import com.hyy.highlightpro.parameter.Constraints
import com.hyy.highlightpro.parameter.HighlightParameter
import com.hyy.highlightpro.parameter.MarginOffset
import com.hyy.highlightpro.shape.RectShape
import com.hyy.highlightpro.util.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.extend.toColorInt
import com.imcys.bilibilias.common.base.utils.SearchParseUtil
import com.imcys.bilibilias.common.base.utils.VideoNumConversionUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.FragmentToolBinding
import com.imcys.bilibilias.databinding.TipAppBinding
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.adapter.ToolItemAdapter
import com.imcys.bilibilias.home.ui.adapter.ViewHolder
import com.imcys.bilibilias.home.ui.fragment.tool.ToolDataHolder
import com.imcys.bilibilias.home.ui.fragment.tool.ToolItem
import com.imcys.bilibilias.home.ui.fragment.tool.ToolViewModel
import com.imcys.bilibilias.view.base.BaseFragment
import com.xiaojinzi.component.anno.RouterAnno
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@RouterAnno(
    hostAndPath = ARouterAddress.ToolFragment,
)
@AndroidEntryPoint
class ToolFragment : BaseFragment<FragmentToolBinding>() {
    private val viewModel by viewModels<ToolViewModel>()

    override val layoutId: Int = R.layout.fragment_tool

    @Inject
    lateinit var networkService: NetworkService

    data class TipBean<T>(
        val title: String,
        val long_title: String,
        val doc: String,
        val face: Drawable,
        val link: String = "",
        val activity: Class<T>? = null
    )

    override fun initView() {
        loadToolItem()

        // 设置监听
        setEditListener()
    }

    override fun initData() {
        viewModel.getOldToolItem()
        initObserver()
    }

    /**
     * 分享检查
     * 如果外部有分享内容，就会在这里过滤
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
     * 设置输入框的搜索监听器
     * 当搜索除发时执行
     */
    private fun setEditListener() {
        binding.fragmentToolEditText.doOnTextChanged { text, _, _, _ ->
            asVideoId(text.toString())
        }
    }

    /**
     * 对输入的视频ID进行解析
     */
    fun asVideoId(text: String) {
        if (text.isBlank()) {
            asToast(requireContext(), getString(R.string.app_ToolFragment_asVideoId))
            return
        }
        when (val type = SearchParseUtil.parse(text)) {
            is SearchParseUtil.SearchType.AV -> viewModel.getView(VideoNumConversionUtils.av2bv(type.id.toLong()))
            is SearchParseUtil.SearchType.BV -> viewModel.getView(type.id)
            is SearchParseUtil.SearchType.EP -> loadEpVideoCard(type.id.toLong())
            is SearchParseUtil.SearchType.ShortLink -> loadShareData(type.url)
            SearchParseUtil.SearchType.None -> Toast.makeText(
                context, getString(R.string.app_ToolFragment_asVideoId2), Toast.LENGTH_SHORT
            )
                .show()
        }
//        mAdapter.submitList(mAdapter.currentList.filter { it.type == 0 })
    }

    /**
     * 加载APP端分享视频
     */
    private fun loadShareData(url: String) {
        HttpUtils.addHeader(COOKIE, asUser.cookie)
            .get(
                url,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Toast.makeText(
                            context,
                            getString(R.string.app_ToolFragment_loadShareData),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val str = response.request.url.toString()
                        asVideoId(str)
                    }
                },
            )
    }

    /**
     * 利用ep号进行检索
     * @param epId Int
     */
    private fun loadEpVideoCard(epId: Long) {
        lifecycleScope.launch(Dispatchers.Default) {
            val bangumiSeasonBean = networkService.n25(epId)

            if (bangumiSeasonBean.code == 0) {
                bangumiSeasonBean.result.episodes.forEach { it1 ->
                    if (it1.id == epId) getVideoCardData()
                }
            }
        }
    }

    fun initObserver() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                ToolDataHolder.videoBaseBean = it.videoInfo
                val adapter =
                    binding.fragmentToolRecyclerView.adapter as ListAdapter<ToolItem, ViewHolder>
                val current = adapter.currentList.filter { it.type == ToolItem.VIEW_TYPE_TOOL }
                adapter.submitList(current + ToolItem("", "", "", 10, 1))
            }
        }
    }

    private fun getVideoCardData() {

    }

    /**
     * 加载支持工具的item
     */
    private fun loadToolItem() {
        val toolItemAdapter = ToolItemAdapter()
        binding.fragmentToolRecyclerView.adapter = toolItemAdapter
        binding.fragmentToolRecyclerView.layoutManager =
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        lifecycleScope.launch {
            viewModel.uiState.collect {
                toolItemAdapter.submitList(it.items.toMutableList())
            }
        }
    }

    private fun loadToolGuide() {
        val tipAppBinding = TipAppBinding.inflate(LayoutInflater.from(activity))
        HighlightPro.with(this)
            .setHighlightParameter {
                tipAppBinding.tipAppTitle.text = getString(R.string.app_guide_tool)
                HighlightParameter.Builder()
                    .setTipsView(tipAppBinding.root)
                    .setHighlightViewId(binding.fragmentToolSearch.id)
                    .setHighlightShape(RectShape(4f.dp, 4f.dp, 6f))
                    .setHighlightHorizontalPadding(8f.dp)
                    .setConstraints(Constraints.BottomToTopOfHighlight + Constraints.EndToEndOfHighlight)
                    .setMarginOffset(MarginOffset(start = 8.dp))
                    .build()
            }
            .setOnDismissCallback {
                (activity as HomeActivity).activityHomeBinding.homeViewPage.currentItem = 0
                (activity as HomeActivity).activityHomeBinding.homeBottomNavigationView.menu.getItem(
                    0,
                ).isCheckable = true
            }
            .setBackgroundColor("#80000000".toColorInt())
            .show()
    }

    companion object {

        @JvmStatic
        fun newInstance() = ToolFragment()
    }
}
