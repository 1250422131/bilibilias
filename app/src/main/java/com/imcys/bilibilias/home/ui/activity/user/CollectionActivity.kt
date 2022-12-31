package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.api.BilibiliApi
import com.imcys.bilibilias.base.app.App
import com.imcys.bilibilias.databinding.ActivityCollectionBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.imcys.bilibilias.home.ui.adapter.CollectionDataAdapter
import com.imcys.bilibilias.home.ui.model.CollectionDataBean
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import com.imcys.bilibilias.utils.HttpUtils
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import kotlin.math.ceil

class CollectionActivity : BaseActivity() {
    private var pn = 0
    private var collectionDataMutableList = mutableListOf<CollectionDataBean.DataBean.MediasBean>()
    private lateinit var binding: ActivityCollectionBinding
    private lateinit var collectionDataAd: CollectionDataAdapter
    private lateinit var userCreateCollectionBean: UserCreateCollectionBean
    private lateinit var createCollectionList: UserCreateCollectionBean.DataBean.ListBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityCollectionBinding?>(this,
            R.layout.activity_collection).apply {
            collectionTopLy.addStatusBarTopPadding()
        }

        initView()
    }

    private fun initView() {
        initCollectionRv()
        loadCollectionList()

    }

    private fun initCollectionRv() {
        binding.apply {
            collectionDataAd = CollectionDataAdapter()
            collectionRecyclerView.adapter = collectionDataAd
            collectionRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            collectionRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isSlideToBottom(recyclerView)) {
                        if (ceil((createCollectionList.media_count / 20).toDouble()) >= pn + 1) {
                            loadCollectionData(createCollectionList)
                        }
                    }
                }
            })

        }
    }

    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }

    private fun loadCollectionList() {
        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.userCreatedScFolderPath}?up_mid=${App.mid}",
                UserCreateCollectionBean::class.java) {
                it.data.list.forEach { it1 ->
                    binding.apply {
                        val tab = collectionTabLayout.newTab()
                        tab.text = it1.title
                        collectionTabLayout.addTab(tab)
                    }
                }
                //初始化全局需要的收藏夹列表
                userCreateCollectionBean = it
                //让监听器可以知道有多少内容加载
                createCollectionList = it.data.list[0]
                //加载第一个收藏夹
                loadCollectionData(it.data.list[0])

                //设置监听器
                binding.apply {
                    collectionTabLayout.addOnTabSelectedListener(object :
                        TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            userCreateCollectionBean.data.list.forEach { it1 ->
                                if (it1.title == tab?.text) {
                                    //更新数据
                                    pn = 0
                                    collectionDataMutableList.clear()
                                    createCollectionList = it1
                                    loadCollectionData(it1)
                                }
                            }
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {
                        }

                    })
                }


            }


    }

    /**
     * 加载收藏夹具体视频内容
     * @param listBean ListBean
     */
    private fun loadCollectionData(listBean: UserCreateCollectionBean.DataBean.ListBean) {

        HttpUtils.addHeader("cookie", App.cookies)
            .get("${BilibiliApi.userCollectionDataPath}?media_id=${listBean.id}&pn=${++pn}&ps=20",
                CollectionDataBean::class.java) {
                collectionDataMutableList.addAll(it.data.medias)
                collectionDataAd.submitList(collectionDataMutableList + mutableListOf())
            }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectionActivity::class.java)
            context.startActivity(intent)
        }

    }

}