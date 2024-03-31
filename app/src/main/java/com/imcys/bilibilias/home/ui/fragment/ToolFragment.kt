package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.utils.AsVideoNumUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.FragmentToolBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import com.imcys.bilibilias.home.ui.activity.tool.MergeVideoActivity
import com.imcys.bilibilias.home.ui.activity.tool.WebAsActivity
import com.imcys.bilibilias.home.ui.adapter.ToolItemAdapter
import com.imcys.bilibilias.home.ui.adapter.ViewHolder
import com.imcys.bilibilias.home.ui.fragment.tool.SearchResultUiState
import com.imcys.bilibilias.home.ui.fragment.tool.ToolViewModel
import com.imcys.bilibilias.home.ui.model.ToolItemBean
import com.imcys.bilibilias.tool_log_export.ui.activity.LogExportActivity
import com.xiaojinzi.component.anno.RouterAnno
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@RouterAnno(
    hostAndPath = ARouterAddress.ToolFragment,
)
@AndroidEntryPoint
class ToolFragment : BaseFragment() {

    private lateinit var fragmentToolBinding: FragmentToolBinding
    private lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ListAdapter<ToolItemBean, ViewHolder>

    @Inject
    lateinit var networkService: NetworkService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val viewModel: ToolViewModel = hiltViewModel()
                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
                val searchResultUiState by viewModel.searchResultUiState.collectAsStateWithLifecycle()
                ToolContent(
                    searchQuery,
                    viewModel::onSearchQueryChanged,
                    viewModel::clearSearches,
                    searchResultUiState
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ToolContent(
        searchQuery: String,
        onSearchQueryChanged: (String) -> Unit,
        onClearSearches: () -> Unit,
        searchResultUiState: SearchResultUiState
    ) {
        Scaffold { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "清空输入框",
                            modifier = Modifier.clickable { onClearSearches() }
                        )
                    }
                )
                when (searchResultUiState) {
                    SearchResultUiState.EmptyQuery -> Unit
                    SearchResultUiState.LoadFailed -> Unit
                    SearchResultUiState.Loading -> Unit
                    is SearchResultUiState.Success ->
                        LazyColumn(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            item {
                                val context = LocalContext.current
                                TextButton(
                                    onClick = {
                                        AsVideoActivity.actionStart(
                                            context,
                                            searchResultUiState.bvid
                                        )
                                    }
                                ) {
                                    Text(text = "点击进入详情页")
                                }
                            }
                            items(searchResultUiState.collection, key = { it.cid }) { item ->
                                val indication = LocalIndication.current
                                Row(
                                    modifier = Modifier
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = indication
                                        ) { }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = item.title)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "去播放页面"
                                    )
                                }
                                HorizontalDivider()
                            }
                        }
                }
            }
        }
    }

    override fun initView() {
        // 加载工具item
//        loadToolItem()

        // 绑定列表
//        mRecyclerView = fragmentToolBinding.fragmentToolRecyclerView

        // 设置监听
//        setEditListener()
    }

    override fun initData() {
    }

    /**
     * 分享检查
     * 如果外部有分享内容，就会在这里过滤
     * @param intent Intent?
     */
    @SuppressLint("ResourceType")
    internal fun parseShare(intent: Intent) {
        lifecycleScope.launchWhenResumed {
            if (Intent.ACTION_SEND == intent.action && "text/plain" == intent.type) {
                asVideoId(intent.getStringExtra(Intent.EXTRA_TEXT).toString())
            }
            intent.extras?.getString("asUrl")?.let { asVideoId(it) }
        }
    }

    /**
     * 设置输入框的搜索监听器
     * 当搜索除发时执行
     */
    private fun setEditListener() {
        fragmentToolBinding.apply {
            fragmentToolEditText.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    asVideoId(textView.text.toString())
                }
                false
            }
        }
    }

    /**
     * 对输入的视频ID进行解析
     * @param inputString String
     */
    fun asVideoId(inputString: String) {
        if (inputString == "") {
            asToast(requireContext(), getString(R.string.app_ToolFragment_asVideoId))
            return
        }

        // ep过滤
        val epRegex = Regex("""(?<=ep)([0-9]+)""")
        // 判断是否有搜到
        if (epRegex.containsMatchIn(inputString)) {
            loadEpVideoCard(epRegex.find(inputString)?.value!!.toLong())
            return
        } else if ("""https://b23.tv/([A-z]|\d)*""".toRegex().containsMatchIn(inputString)) {
            loadShareData(
                """https://b23.tv/([A-z]|\d)*""".toRegex()
                    .find(inputString)?.value!!.toString(),
            )
            return
        } else if (AsVideoNumUtils.getBvid(inputString) != "") {
            getVideoCardData(AsVideoNumUtils.getBvid(inputString))
            return
        }

//        val liveRegex = Regex("""(?<=live.bilibili.com/)(\d+)""")
//        //判断是否有搜到
//        if (liveRegex.containsMatchIn(inputString)) {
//            loadLiveRoomCard(liveRegex.find(inputString)?.value!!.toString())
//            return
//        }
        // 至此，视频的检索没有超过，开始判断是不是直播内容

        mAdapter.apply {
            currentList.filter { it.type == 0 }.run {
                submitList(this)
            }
        }
        Toast.makeText(context, getString(R.string.app_ToolFragment_asVideoId2), Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * 加载APP端分享视频
     * @param toString String
     */
    private fun loadShareData(toString: String) {
        HttpUtils.addHeader(COOKIE, asUser.cookie)
            .get(
                toString,
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
                    if (it1.id == epId) getVideoCardData(it1.bvid)
                }
            }
        }
    }

    private fun getVideoCardData(bvid: String) {
        fragmentToolBinding.apply {
            launchUI {
                val videoBaseBean = networkService.n26(bvid)

                (mAdapter).apply {
                    // 这里的理解，filter过滤掉之前的特殊item，只留下功能模块，这里条件可以叠加。
                    // run函数将新准备的视频item合并进去，并返回。
                    // 最终apply利用该段返回执行最外层apply的submitList方法
                    currentList.filter { it.type == 0 }.run {
                        mutableListOf(
                            ToolItemBean(
                                type = 1,
                                videoBaseBean = videoBaseBean,
                                clickEvent = {
                                },
                            ),
                        ) + this
                    }.apply {
                        submitList(this)
                    }
                }
            }
        }
    }

    /**
     * 加载支持工具的item
     */
    private fun loadToolItem() {
        val toolItemMutableList = mutableListOf<ToolItemBean>()
        launchUI {
            // 通过远程数据获取item
            val oldToolItemBean = withContext(Dispatchers.IO) {
                networkService.getOldToolItem()
            }

            oldToolItemBean.data.forEach {
                when (it.tool_code) {
                    // 视频解析
                    1 -> {
                        toolItemMutableList.add(
                            ToolItemBean(
                                it.title,
                                it.img_url,
                                it.color,
                            ) {
                                asVideoId(fragmentToolBinding.fragmentToolEditText.text.toString())
                            },
                        )
                    }
                    // 设置
                    2 -> {
                        toolItemMutableList.add(
                            ToolItemBean(
                                it.title,
                                it.img_url,
                                it.color,
                            ) {
                                val intent = Intent(context, SettingActivity::class.java)
                                requireActivity().startActivity(intent)
                            },
                        )
                    }
                    // web解析
                    3 -> {
                        toolItemMutableList.add(
                            ToolItemBean(
                                it.title,
                                it.img_url,
                                it.color,
                            ) {
                                val intent = Intent(context, WebAsActivity::class.java)
                                requireActivity().startActivity(intent)
                            },
                        )
                    }
                    // 导出日志
                    4 -> {
                        toolItemMutableList.add(
                            ToolItemBean(
                                it.title,
                                it.img_url,
                                it.color,
                            ) {
                                LogExportActivity.actionStart(requireContext())
                            },
                        )
                    }
                    // 独立合并
                    5 -> {
                        toolItemMutableList.add(
                            ToolItemBean(
                                it.title,
                                it.img_url,
                                it.color,
                            ) {
                                MergeVideoActivity.actionStart(requireContext())
                            },
                        )
                    }
                }
            }

            // 展示item
            fragmentToolBinding.apply {
                fragmentToolRecyclerView.adapter = ToolItemAdapter()

                mAdapter = ((mRecyclerView.adapter) as ToolItemAdapter)
                mAdapter.submitList(toolItemMutableList)

                fragmentToolRecyclerView.layoutManager =
                    GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return when ((mAdapter.currentList)[position].type) {
                                    1 -> 3
                                    2 -> 3
                                    else -> 1
                                }
                            }
                        }
                    }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ToolFragment()
    }
}
