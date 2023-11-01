package com.imcys.bilibilias.home.ui.activity.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.asUser
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.model.Collections
import com.imcys.bilibilias.common.base.utils.http.HttpUtils
import com.imcys.bilibilias.databinding.ActivityCollectionBinding
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import com.imcys.bilibilias.view.base.BaseActivity
import com.zackratos.ultimatebarx.ultimatebarx.addStatusBarTopPadding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

// 收藏夹
@AndroidEntryPoint
class CollectionActivity : BaseActivity<ActivityCollectionBinding>() {

    private var pn = 0
    private var collectionDataMutableList = mutableListOf<Collections.Media>()


    private lateinit var createCollectionList: UserCreateCollectionBean.Collection
    override fun getLayoutRes(): Int = R.layout.activity_collection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.collectionTopLy.addStatusBarTopPadding()
    }

    override fun initView() {
        initCollectionRv()
        loadCollectionList()
    }

    private fun initCollectionRv() {
        binding.apply {
            collectionRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            collectionRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isSlideToBottom(recyclerView)) {
                        if (ceil((createCollectionList.mediaCount / 20).toDouble()) >= pn + 1) {
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
    }

    /**
     * 加载收藏夹具体视频内容
     * @param listBean ListBean
     */
    private fun loadCollectionData(listBean: UserCreateCollectionBean.Collection) {
        HttpUtils.addHeader(COOKIE, asUser.cookie)
            .get(
                "${BilibiliApi.getFavoritesContentList}?media_id=${listBean.id}&pn=${++pn}&ps=20",
                Collections::class.java,
            ) {
            }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectionActivity::class.java)
            context.startActivity(intent)
        }
    }
}
