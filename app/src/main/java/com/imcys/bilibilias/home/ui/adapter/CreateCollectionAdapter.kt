package com.imcys.bilibilias.home.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemCollectionBinding
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean

class CreateCollectionAdapter(
    val datas: MutableList<UserCreateCollectionBean.DataBean.ListBean>,
    val selectedResult: (position: Int, itemBinding: ItemCollectionBinding) -> Unit,
) :
    RecyclerView.Adapter<CreateCollectionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCollectionBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_collection, parent, false
            )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemCollectionBinding>(holder.itemView)
        binding?.apply {
            listBean = datas[position]
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