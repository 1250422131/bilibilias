package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemDlBangumiPageBinding
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean


class BangumiPageAdapter(
    val datas: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>,
    val selectedResult: (position: Int, itemBinding: ItemDlBangumiPageBinding) -> Unit,
) :
    RecyclerView.Adapter<BangumiPageAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDlBangumiPageBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_dl_bangumi_page, parent, false
            )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemDlBangumiPageBinding>(holder.itemView)

        binding?.apply {
            episodesBean = datas[position]
        }

        //回调点击项数
        binding?.itemCollectionButton?.setOnClickListener {
            selectedResult(position, binding)
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}