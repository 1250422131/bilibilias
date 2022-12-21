package com.imcys.bilibilias.home.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.databinding.FragmentToolBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.adapter.ToolItemAdapter
import com.imcys.bilibilias.home.ui.adapter.ViewHolder
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.ToolItemBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.imcys.bilibilias.home.ui.model.view.ToolViewHolder
import com.imcys.bilibilias.utils.AsVideoNumUtils
import com.imcys.bilibilias.utils.HttpUtils
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding


class ToolFragment : Fragment() {

    lateinit var fragmentToolBinding: FragmentToolBinding
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ListAdapter<ToolItemBean, ViewHolder>
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

        //加载工具item
        loadToolItem()
        //设置监听
        setEditListener()

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
    private fun asVideoId(inputString: String) {
        //ep过滤
        val epRegex = Regex("""(?<=ep)([0-9]+)""")
        //判断是否有搜到
        if (epRegex.containsMatchIn(inputString)) {
            loadEpVideoCard(epRegex.find(inputString)?.value!!.toInt())
        } else {
            if (AsVideoNumUtils.getBvid(inputString) != "") {
                getVideoCardData(AsVideoNumUtils.getBvid(inputString))
            } else {
                mAdapter.apply {
                    currentList.filter { it.type == 0 }.run {
                        submitList(this)
                    }
                }
                Toast.makeText(context, "输入的内容不正确", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * 利用ep号进行检索
     * @param epId Int
     */
    private fun loadEpVideoCard(epId: Int) {
        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.bangumiVideoDataPath}?ep_id=$epId", BangumiSeasonBean::class.java) {
                if (it.code == 0) {
                    it.result.episodes.forEach { it1 ->
                        if (it1.id == epId) getVideoCardData(it1.bvid)
                    }
                }
            }
    }

    private fun getVideoCardData(bvid: String) {

        fragmentToolBinding.apply {
            HttpUtils.addHeader("cookie", App.cookies)
                .get(BilibiliApi.getVideoDataPath + "?bvid=$bvid", VideoBaseBean::class.java) {
                    (mAdapter).apply {
                        //这里的理解，filter过滤掉之前的特殊item，只留下功能模块，这里条件可以叠加。
                        //run函数将新准备的视频item合并进去，并返回。
                        //最终apply利用该段返回执行最外层apply的submitList方法
                        currentList.filter { it.type == 0 }.run {
                            mutableListOf(
                                ToolItemBean(
                                    type = 1,
                                    videoBaseBean = it
                                )
                            ) + this
                        }.apply {
                            submitList(this)
                        }

                    }
                }
        }

    }


    private fun loadToolItem() {

        val toolItemMutableList = mutableListOf(
            ToolItemBean("缓 存 视 频", "https://s1.ax1x.com/2022/12/18/zbTmpF.png", "") {
                mAdapter.currentList[0].videoBaseBean?.data?.bvid?.let {
                    context?.let { it1 -> AsVideoActivity.actionStart(it1, it) }
                } ?: run {
                    asVideoId(fragmentToolBinding.fragmentToolEditText.text.toString())
                }

            }

        )

        fragmentToolBinding.apply {
            fragmentToolRecyclerView.adapter = ToolItemAdapter()

            mAdapter = ((mRecyclerView.adapter) as ToolItemAdapter)
            mAdapter.submitList(toolItemMutableList)

            fragmentToolRecyclerView.layoutManager =
                GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if ((mAdapter.currentList)[position].type == 1) {
                                3
                            } else {
                                2
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