package com.imcys.bilibilias.home.ui.activity.user

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ActivityCollectionBinding
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import com.imcys.bilibilias.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

// 收藏夹
@AndroidEntryPoint
class CollectionActivity : BaseActivity<ActivityCollectionBinding>() {

    private var pn = 0


    private lateinit var createCollectionList: UserCreateCollectionBean.Collection
    override fun getLayoutRes(): Int = R.layout.activity_collection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}
