package com.imcys.bilibilias.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.databinding.ItemVideoDefinitionBinding

class VideoDefinitionAdapter(
    private val datas: List<String>,
    val selectedResult: (position: Int, beforeChangePosition: Int) -> Unit,
) :
    RecyclerView.Adapter<VideoDefinitionAdapter.ViewHolder>() {

    var selectItem = -1
    private lateinit var selectBinding: ItemVideoDefinitionBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemVideoDefinitionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_video_definition, parent, false
            )


        return ViewHolder(binding.root)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<ItemVideoDefinitionBinding>(holder.itemView)
        binding?.apply {
            itemCollectionButton.text = datas[position]

            binding.itemCollectionButton.setOnClickListener {

                if (selectItem != -1) {
                    selectBinding.itemCollectionButton.setBackgroundResource(R.color.color_primary_variant)
                }
                binding.itemCollectionButton.setBackgroundResource(R.color.color_primary)

                //先向外传递
                selectedResult(position, selectItem)
                //再改变记录
                selectBinding = binding
                selectItem = position

            }
        }

    }

    override fun getItemCount(): Int {
        return datas.size
    }


}