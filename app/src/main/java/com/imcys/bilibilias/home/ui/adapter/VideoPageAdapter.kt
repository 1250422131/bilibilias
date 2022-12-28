package com.imcys.bilibilias.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemDlVideoPageBinding
import com.imcys.bilibilias.home.ui.model.VideoPageListData

class VideoPageAdapter(
    val datas: MutableList<VideoPageListData.DataBean>,
    val selectedResult: (position: Int, itemBinding: MutableList<ItemDlVideoPageBinding>) -> Unit,
) : RecyclerView.Adapter<VideoPageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var longClickItem = -1
    private val bindingMutableList = mutableListOf<ItemDlVideoPageBinding>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDlVideoPageBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_dl_video_page, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        val binding = DataBindingUtil.getBinding<ItemDlVideoPageBinding>(holder.itemView)
        binding?.apply {
            dataBean = datas[position]
        }


        //回调点击项数
        binding?.apply {
            bindingMutableList.add(this)
            itemCollectionButton.setOnClickListener {
                selectedResult(position, bindingMutableList)
            }

        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}