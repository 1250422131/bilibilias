package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.baidu.mobstat.StatService
import com.google.android.material.tabs.TabLayout
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.BaseActivity
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.databinding.ActivityCollectionBinding
import com.imcys.bilibilias.home.ui.adapter.CollectionDataAdapter
import com.imcys.bilibilias.home.ui.model.CollectionDataBean
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.ceil

// 收藏夹
@AndroidEntryPoint
class CollectionActivity : BaseActivity<ActivityCollectionBinding>() {
    override val layoutId: Int =   R.layout.activity_collection
    private var pn = 0
    private var collectionDataMutableList = mutableListOf<CollectionDataBean.DataBean.MediasBean>()

    @Inject
    lateinit var collectionDataAd: CollectionDataAdapter
    private lateinit var createCollectionList: UserCreateCollectionBean.DataBean.ListBean

    @Inject
    lateinit var networkService: NetworkService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding .collectionTopLy.addStatusBarTopPadding()
    }

    override fun initView() {
        initCollectionRv()
        loadCollectionList()
    }

    private fun initCollectionRv() {
        binding.apply {
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
        launchUI {
            val userCreateCollectionBean = networkService.n17()

            userCreateCollectionBean.data.list.forEach { it1 ->
                binding.apply {
                    val tab = collectionTabLayout.newTab()
                    tab.text = it1.title
                    collectionTabLayout.addTab(tab)
                }
            }

            // 让监听器可以知道有多少内容加载
            createCollectionList = userCreateCollectionBean.data.list[0]
            // 加载第一个收藏夹
            loadCollectionData(userCreateCollectionBean.data.list[0])

            // 设置监听器
            binding.apply {
                // 这里监听选择的是哪个收藏夹
                collectionTabLayout.addOnTabSelectedListener(
                    object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            userCreateCollectionBean.data.list.forEach { it1 ->
                                if (it1.title == tab?.text) {
                                    // 更新数据
                                    pn = 0
                                    collectionDataMutableList.clear()
                                    createCollectionList = it1
                                    loadCollectionData(it1)
                                }
                            }
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
                    }
                )
            }
        }
    }

    /**
     * 加载收藏夹具体视频内容
     *
     * @param listBean ListBean
     */
    private fun loadCollectionData(listBean: UserCreateCollectionBean.DataBean.ListBean) {

        launchUI {
            val userCollection = networkService.getUserCollection(listBean.id, ++pn)
            userCollection.data.medias?.also { collectionDataMutableList.addAll(it) }
            collectionDataAd.submitList(collectionDataMutableList + mutableListOf())

            binding.collectionRecyclerView.scrollToPosition(1)
        }
    }

    override fun onResume() {
        super.onResume()
        StatService.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        StatService.onPause(this)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectionActivity::class.java)
            context.startActivity(intent)
        }
    }
}
