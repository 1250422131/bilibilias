package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mobstat.StatService
import com.google.android.material.textfield.TextInputLayout
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.base.utils.DialogUtils
import com.imcys.bilibilias.base.utils.DownloadQueue
import com.imcys.bilibilias.common.base.BaseFragment
import com.imcys.bilibilias.common.base.utils.AsRegexUtil
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.common.base.utils.TextType
import com.imcys.bilibilias.common.base.utils.asLogD
import com.imcys.bilibilias.databinding.FragmentToolBinding
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import com.imcys.bilibilias.home.ui.activity.tool.MergeVideoActivity
import com.imcys.bilibilias.home.ui.activity.tool.WebAsActivity
import com.imcys.bilibilias.home.ui.adapter.ToolItemAdapter
import com.imcys.bilibilias.home.ui.adapter.ViewHolder
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.tool_log_export.ui.activity.LogExportActivity
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ToolFragment : BaseFragment() {

    private lateinit var fragmentToolBinding: FragmentToolBinding
    private lateinit var mRecyclerView: RecyclerView
    private var isInitialized = false
    private var sharedIntent: Intent? = null

    lateinit var mAdapter: ListAdapter<ToolItemBean, ViewHolder>

    @Inject
    lateinit var networkService: NetworkService

    @Inject
    lateinit var downloadQueue: DownloadQueue

    private val regex1 = Regex("""(?:^|/)ep([0-9]+)""")
    private val regex2 = Regex("""https://b23.tv/([A-z]|\d)*""")
    private val regex3 = Regex("""[space.bilibili.com/]?(\d+).*""")

    @SuppressLint("CommitPrefEdits")
    override fun onResume() {
        super.onResume()
        StatService.onPageStart(context, "ToolFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        fragmentToolBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tool, container, false)

        initView()

        return fragmentToolBinding.root
    }

    private fun initView() {
        DialogUtils.downloadQueue = downloadQueue

        // 设置布局不浸入
        fragmentToolBinding.fragmentToolTopLinearLayout.addStatusBarTopPadding()

        // 加载工具item
        loadToolItem()

        // 绑定列表
        mRecyclerView = fragmentToolBinding.fragmentToolRecyclerView

        // 设置监听
        setEditListener()
    }

    /**
     * 分享检查
     * 如果外部有分享内容，就会在这里过滤
     */
    @SuppressLint("ResourceType")
    internal fun parseShare(intent: Intent?) {
        val action = intent?.action
        val type = intent?.type

        // 下面这段代表是从浏览器解析过来的
        intent?.extras?.getString("asUrl")?.let {
            asVideoId(it)
        }

        if (isInitialized) {
            if (Intent.ACTION_SEND == action && type != null) {
                if ("text/plain" == type) {
                    asVideoId(intent.getStringExtra(Intent.EXTRA_TEXT).toString())
                }
            }
            if (Intent.ACTION_CREATE_SHORTCUT == intent?.action) {
                getUrlFromClipboard()
            }
        } else {
            sharedIntent = intent
        }
    }

    /**
     * 设置输入框的搜索监听器
     * 当搜索除发时执行
     */
    @OptIn(FlowPreview::class)
    private fun setEditListener() {
        val editText = fragmentToolBinding.fragmentToolEditText
            .editText ?: return

        editText.textChangeFlow()
            .filter { it.isNotEmpty() }
            .debounce(300)
            .map { asVideoId(it) }
            .flowOn(Dispatchers.IO)
            .launchIn(lifecycleScope)
    }

    /**
     * 对输入的视频ID进行解析
     */
    fun asVideoId(text: String) {
        // 判断是否有搜到
        when (val result = AsRegexUtil.parse(text)) {
            is TextType.AV -> getVideoCardData(NewVideoNumConversionUtils.av2bv(result.text))
            is TextType.BV -> getVideoCardData(result.text)
            is TextType.EP -> loadEpVideoCard(result.text)
            is TextType.ShortLink -> loadShareData(result.text)
            is TextType.UserSpace -> loadUserCardData(result.text)
            null -> {
                lifecycleScope.launch {
                    mAdapter.currentList.filter { it.type == 0 }.run {
                        mAdapter.submitList(this)
                    }
                    Toast.makeText(
                        context,
                        R.string.app_ToolFragment_asVideoId2,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadUserCardData(inputString: String) {
        launchUI {
            val userCardBean = networkService.getUserCardData(inputString.toLong())
            (mAdapter).apply {
                // 这里的理解，filter过滤掉之前的特殊item，只留下功能模块，这里条件可以叠加。
                // run函数将新准备的视频item合并进去，并返回。
                // 最终apply利用该段返回执行最外层apply的submitList方法
                currentList.filter { it.type == 0 }.run {
                    mutableListOf(
                        ToolItemBean(
                            type = 3,
                            userCardBean = userCardBean.data.card,
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

    /**
     * 加载APP端分享视频
     */
    private fun loadShareData(url: String) {
        asLogD("调试",url)
        lifecycleScope.launch {
            runCatching { networkService.shortLink(url) }
                .onSuccess { asVideoId(it) }
                .onFailure {
                    Toast.makeText(
                        context,
                        getString(R.string.app_ToolFragment_loadShareData),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }
    }

    /**
     * 利用ep号进行检索
     * @param epId Int
     */
    private fun loadEpVideoCard(epId: Long) {
        lifecycleScope.launch(Dispatchers.Default) {
            val bangumiSeasonBean = networkService.getBangumiSeasonBeanByEpid(epId)

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
                                // asVideoId(fragmentToolBinding.fragmentToolEditText.text.toString())
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
                                    3 -> 3
                                    else -> 1
                                }
                            }
                        }
                    }

                isInitialized = true

                // 如果在初始化前有分享信息需要处理，则在此处理
                sharedIntent?.let { parseShare(it) }
            }
        }
    }

    private fun getUrlFromClipboard() {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboard.primaryClip
        if (clipData == null || clipData.itemCount == 0) {
            return
        }
        clipData.getItemAt(0).text?.let {
            // fragmentToolBinding.fragmentToolEditText.setText(it)
            asVideoId(it.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StatService.onPageEnd(context, getString(R.string.app_ToolFragment_onDestroy))
    }

    // 构建输入框文字变化流
    private fun TextInputLayout.textChangeFlow(): Flow<String> = callbackFlow {
        val textWatcher = TextInputLayout.OnEditTextAttachedListener {
            trySend(it.editText?.text.toString())
        }
        addOnEditTextAttachedListener(textWatcher)
        awaitClose { removeOnEditTextAttachedListener(textWatcher) }
    }

    // 构建输入框文字变化流
    fun EditText.textChangeFlow(): Flow<String> = callbackFlow {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                trySend(s.toString())
            }
        }
        addTextChangedListener(watcher)
        awaitClose { removeTextChangedListener(watcher) }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ToolFragment()
    }
}
