package com.imcys.bilibilias.home.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.utils.AsVideoNumUtils
import com.imcys.bilibilias.databinding.ItemToneQualityBinding
import com.imcys.bilibilias.home.ui.model.DashVideoPlayBean

class VideoToneQualityAdapter(
    private val videoAudioBeans: MutableList<DashVideoPlayBean.DataBean.DashBean.AudioBean>,
    val selectedResult: (audio: DashVideoPlayBean.DataBean.DashBean.AudioBean) -> Unit,
) :
    RecyclerView.Adapter<ViewHolder>() {
    private var selectedItem = 0
    lateinit var selectedBinding: ItemToneQualityBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemToneQualityBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_tone_quality, parent, false
            )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        DataBindingUtil.getBinding<ItemToneQualityBinding>(holder.itemView)?.apply {

            audioBean = videoAudioBeans[position]

            if (position == 0 && selectedItem == 0) {
                selectedBinding = this
                this.audioBean?.selected = 1
            }

            itemToneQualityButton.text =
                AsVideoNumUtils.getQualityName(videoAudioBeans[position].id)


            itemToneQualityButton.setOnClickListener {
                if (position != selectedItem) {

                    selectedBinding.audioBean?.selected = 0
                    this@VideoToneQualityAdapter.notifyItemChanged(selectedItem)


                    selectedBinding = this
                    selectedItem = position

                    selectedBinding.audioBean?.selected = 1
                    this@VideoToneQualityAdapter.notifyItemChanged(position)

                    selectedResult(videoAudioBeans[position])
                }
            }

        }

    }


    override fun getItemCount(): Int {
        return videoAudioBeans.size
    }
}