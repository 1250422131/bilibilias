package com.imcys.bilibilias.home.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baidu.mobstat.StatService
import com.hyy.highlightpro.HighlightPro
import com.hyy.highlightpro.parameter.Constraints
import com.hyy.highlightpro.parameter.HighlightParameter
import com.hyy.highlightpro.parameter.MarginOffset
import com.hyy.highlightpro.shape.RectShape
import com.hyy.highlightpro.util.dp
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.base.utils.asToast
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.arouter.ARouterAddress
import com.imcys.bilibilias.common.base.extend.toColorInt
import com.imcys.bilibilias.common.base.utils.AsVideoNumUtils
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.databinding.FragmentToolBinding
import com.imcys.bilibilias.databinding.TipAppBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.activity.HomeActivity
import com.imcys.bilibilias.home.ui.activity.SettingActivity
import com.imcys.bilibilias.home.ui.activity.tool.MergeVideoActivity
import com.imcys.bilibilias.home.ui.activity.tool.WebAsActivity
import com.imcys.bilibilias.home.ui.adapter.ToolItemAdapter
import com.imcys.bilibilias.home.ui.adapter.ViewHolder
import com.imcys.bilibilias.home.ui.model.*
import com.imcys.bilibilias.home.ui.model.view.ToolViewHolder
import com.imcys.bilibilias.tool_livestream.ui.activity.LiveStreamActivity
import com.imcys.bilibilias.tool_livestream.ui.model.LiveRoomDataBean
import com.imcys.bilibilias.tool_log_export.ui.activity.LogExportActivity
import com.xiaojinzi.component.anno.RouterAnno
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.dkzwm.widget.srl.RefreshingListenerAdapter
import me.dkzwm.widget.srl.extra.header.MaterialHeader
import me.dkzwm.widget.srl.indicator.IIndicator
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

@RouterAnno(
    hostAndPath = ARouterAddress.ToolFragment,
)
class ToolFragment : Fragment() {

    lateinit var fragmentToolBinding: FragmentToolBinding
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ListAdapter<ToolItemBean, ViewHolder>

    @SuppressLint("CommitPrefEdits")
    override fun onResume() {
        super.onResume()
        //这里仍然是在判断是否有被引导过了
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
                (activity as HomeActivity).activityHomeBinding.homeViewPage.currentItem = 0
                (activity as HomeActivity).activityHomeBinding.homeBottomNavigationView.menu.getItem(
                    0
                ).isCheckable = true
            }
            .setBackgroundColor("#80000000".toColorInt())
            .show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        fragmentToolBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tool, container, false)

        initView()

        return fragmentToolBinding.root
    }

    private fun initView() {
        //设置布局不浸入
        fragmentToolBinding.fragmentToolTopLy.addStatusBarTopPadding()

        //设置点击事件
        fragmentToolBinding.toolViewHolder =
            context?.let { ToolViewHolder(it, fragmentToolBinding) }

        //绑定列表
        mRecyclerView = fragmentToolBinding.fragmentToolRecyclerView

        //设置监听
        setEditListener()

        //加载工具item
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
            //下面这段代表是从浏览器解析过来的
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

        //ep过滤
        val epRegex = Regex("""(?<=ep)([0-9]+)""")
        //判断是否有搜到
        if (epRegex.containsMatchIn(inputString)) {
            loadEpVideoCard(epRegex.find(inputString)?.value!!.toLong())
            return
        } else if ("""https://b23.tv/([A-z]|\d)*""".toRegex().containsMatchIn(inputString)) {
            loadShareData(
                """https://b23.tv/([A-z]|\d)*""".toRegex()
                    .find(inputString)?.value!!.toString()
            )
            return
        } else if (AsVideoNumUtils.getBvid(inputString) != "") {
            getVideoCardData(AsVideoNumUtils.getBvid(inputString))
            return
        }

        val liveRegex = Regex("""(?<=live.bilibili.com/)(\d+)""")
        //判断是否有搜到
        if (liveRegex.containsMatchIn(inputString)) {
            loadLiveRoomCard(liveRegex.find(inputString)?.value!!.toString())
            return
        }
        //至此，视频的检索没有超过，开始判断是不是直播内容


        mAdapter.apply {
            currentList.filter { it.type == 0 }.run {
                submitList(this)
            }
        }
        Toast.makeText(context, getString(R.string.app_ToolFragment_asVideoId2), Toast.LENGTH_SHORT)
            .show()

    }

    private fun loadLiveRoomCard(roomId: String) {
        lifecycleScope.launch {
            //获取数据
            val liveRoomData = getLiveRoomData(roomId)
            mAdapter.apply {
                currentList.filter { it.type == 0 }.run {
                    //由于返回list不可变，这里采用相加
                    mutableListOf(
                        ToolItemBean(
                            type = 2,
                            liveRoomDataBean = liveRoomData,
                            clickEvent = {
                                LiveStreamActivity.actionStart(requireContext(), roomId)
                            }
                        )
                    ) + this
                }.apply {
                    submitList(this)
                }
            }


        }

    }


    private suspend fun getLiveRoomData(roomId: String): LiveRoomDataBean {
        return withContext(lifecycleScope.coroutineContext) {
            HttpUtils.addHeader("cookie", (context as HomeActivity).asUser.cookie)
                .asyncGet(
                    "${BilibiliApi.liveRoomDataPath}?room_id=$roomId",
                    LiveRoomDataBean::class.java
                )
        }
    }

    /**
     * 加载APP端分享视频
     * @param toString String
     */
    private fun loadShareData(toString: String) {
        HttpUtils.addHeader("cookie", (context as HomeActivity).asUser.cookie)
            .get(toString, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(
                        context, getString(R.string.app_ToolFragment_loadShareData),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val str = response.request.url.toString()
                    asVideoId(str)
                }
            })
    }


    /**
     * 利用ep号进行检索
     * @param epId Int
     */
    private fun loadEpVideoCard(epId: Long) {
        lifecycleScope.launch(Dispatchers.Default) {

            val bangumiSeasonBean =
                KtHttpUtils.addHeader("cookie", (context as HomeActivity).asUser.cookie)
                    .asyncGet<BangumiSeasonBean>("${BilibiliApi.bangumiVideoDataPath}?ep_id=$epId")

            if (bangumiSeasonBean.code == 0) {
                bangumiSeasonBean.result.episodes.forEach { it1 ->
                    if (it1.id == epId) getVideoCardData(it1.bvid)
                }
            }

        }


    }

    private fun getVideoCardData(bvid: String) {

        fragmentToolBinding.apply {

            lifecycleScope.launch {
                val videoBaseBean =
                    KtHttpUtils.addHeader("cookie", (context as HomeActivity).asUser.cookie)
                        .asyncGet<VideoBaseBean>(BilibiliApi.getVideoDataPath + "?bvid=$bvid")
                (mAdapter).apply {
                    //这里的理解，filter过滤掉之前的特殊item，只留下功能模块，这里条件可以叠加。
                    //run函数将新准备的视频item合并进去，并返回。
                    //最终apply利用该段返回执行最外层apply的submitList方法
                    currentList.filter { it.type == 0 }.run {
                        mutableListOf(
                            ToolItemBean(
                                type = 1,
                                videoBaseBean = videoBaseBean,
                                clickEvent = {

                                }
                            )
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
        lifecycleScope.launch {
            //通过远程数据获取item
            val oldToolItemBean = getOldToolItemBean()
            oldToolItemBean.data.forEach {
                when (it.tool_code) {
                    //视频解析
                    1 -> {
                        toolItemMutableList.add(ToolItemBean(
                            it.title,
                            it.img_url,
                            it.color
                        ) {
                            asVideoId(fragmentToolBinding.fragmentToolEditText.text.toString())
                        })
                    }
                    //设置
                    2 -> {
                        toolItemMutableList.add(ToolItemBean(
                            it.title,
                            it.img_url,
                            it.color
                        ) {
                            val intent = Intent(context, SettingActivity::class.java)
                            requireActivity().startActivity(intent)
                        })
                    }
                    //web解析
                    3 -> {
                        toolItemMutableList.add(ToolItemBean(
                            it.title,
                            it.img_url,
                            it.color
                        ) {
                            val intent = Intent(context, WebAsActivity::class.java)
                            requireActivity().startActivity(intent)
                        })
                    }
                    //导出日志
                    4 -> {
                        toolItemMutableList.add(ToolItemBean(
                            it.title,
                            it.img_url,
                            it.color
                        ) {
                            LogExportActivity.actionStart(requireContext())
                        })
                    }
                    //独立合并
                    5 -> {
                        toolItemMutableList.add(ToolItemBean(
                            it.title,
                            it.img_url,
                            it.color
                        ) {
                            MergeVideoActivity.actionStart(requireContext())
                        })
                    }
                }
            }

            //展示item
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


    private suspend fun getOldToolItemBean(): OldToolItemBean {
        return withContext(lifecycleScope.coroutineContext) {
            HttpUtils.asyncGet(
                "${BiliBiliAsApi.appFunction}?type=oldToolItem",
                OldToolItemBean::class.java
            )
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