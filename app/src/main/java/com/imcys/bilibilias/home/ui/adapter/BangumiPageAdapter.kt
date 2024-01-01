package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.databinding.ItemDlBangumiPageBinding
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean

class BangumiPageAdapter(
    private val datas: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>,
    private val selectedResult: (position: Int, itemBinding: ViewHolder) -> Unit,
) : RecyclerView.Adapter<BangumiPageAdapter.ViewHolder>() {
    class ViewHolder(view: ItemDlBangumiPageBinding) : RecyclerView.ViewHolder(view.root) {
        var episodesBean: BangumiSeasonBean.ResultBean.EpisodesBean? = view.episodesBean
        private val itemCollectionButton = view.itemCollectionButton
        fun bind(
            episodesBean: BangumiSeasonBean.ResultBean.EpisodesBean,
            selectedResult: (position: Int, itemBinding: ViewHolder) -> Unit
        ) {
            this.episodesBean = episodesBean
            itemCollectionButton.setOnClickListener {
                selectedResult(layoutPosition, this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDlBangumiPageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position], selectedResult)
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}
