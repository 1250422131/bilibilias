package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemBangumiSubsectionBinding
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean

class BangumiSubsectionAdapter(
    val datas: MutableList<BangumiSeasonBean.ResultBean.EpisodesBean>,
    val defaultCid: Int,
    val onClickMethod: (datas: BangumiSeasonBean.ResultBean.EpisodesBean, position: Int) -> Unit,
) : RecyclerView.Adapter<BangumiSubsectionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var clickItem = -1


    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemBangumiSubsectionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_bangumi_subsection, parent, false
            )

        return ViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemBangumiSubsectionBinding>(holder.itemView)

        if (clickItem == -1) {
            datas.forEachIndexed { index, episodesBean ->
                if (defaultCid == episodesBean.cid) {
                    datas[index].checkState = 1
                    clickItem = index
                }
            }
        }

        binding?.episodesBean = datas[position]


        holder.itemView.setOnClickListener {

            if (clickItem == -1) {
                datas[0].checkState = 0
                clickItem = 0
            }

            datas[clickItem].checkState = 0
            notifyItemChanged(clickItem)

            clickItem = position

            datas[position].checkState = 1

            notifyItemChanged(position)
            onClickMethod(datas[position], position)
        }

        binding?.executePendingBindings()
    }
}