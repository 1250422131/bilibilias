package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemSubsectionBinding
import com.imcys.bilibilias.home.ui.model.VideoPageListData

class SubsectionAdapter(
    val datas: MutableList<VideoPageListData.DataBean>,
    val onClickMethod: (datas: VideoPageListData.DataBean, position: Int) -> Unit,
) :
    RecyclerView.Adapter<SubsectionAdapter.ViewHolder>() {

    var clickItem = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemSubsectionBinding>(LayoutInflater.from(parent.context),
                R.layout.item_subsection, parent, false)

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemSubsectionBinding>(holder.itemView)

        binding?.dataBean = datas[position]
        if (position == 0) {
            datas[0].checkState = 1
        }

        holder.itemView.setOnClickListener {
            datas[clickItem].checkState = 0

            clickItem = position

            datas[position].checkState = 1
            onClickMethod(datas[position], position)
        }

        binding?.executePendingBindings()

    }

    override fun getItemCount(): Int {
        return datas.size
    }
}