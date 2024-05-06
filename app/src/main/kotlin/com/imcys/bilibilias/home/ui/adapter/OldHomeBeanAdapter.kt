package com.imcys.bilibilias.home.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.imcys.bilibilias.core.model.bilibilias.HomeBanner
import com.imcys.bilibilias.databinding.ItemHomeBannerBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.youth.banner.adapter.BannerAdapter

class OldHomeBeanAdapter(
    private val aggregatingData: HomeBanner,
) : BannerAdapter<String, OldHomeBeanAdapter.BannerViewHolder>(aggregatingData.textList) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding =
            ItemHomeBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindView(holder: BannerViewHolder, data: String, position: Int, size: Int) {
        holder.bind(aggregatingData)
    }

    class BannerViewHolder(binding: ItemHomeBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        private val bannerTitle = binding.itemHomeBannerTitle
        private val bannerImage = binding.itemHomeBannerImage
        fun bind(aggregatingData: HomeBanner) {
            val position = position
            bannerTitle.text = aggregatingData.textList[position]
            bannerImage.load(aggregatingData.imgUrlList[position])
            itemView.setOnClickListener {
                setEvent(aggregatingData.typeList[position], position, it.context, aggregatingData)
            }
        }

        private fun setEvent(
            type: String,
            position: Int,
            context: Context,
            aggregatingData: HomeBanner
        ) {
            when (type) {
                "goBilibili" -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(
                        Uri.parse(aggregatingData.dataList[position]),
                        "text/plain"
                    )
                    context.startActivity(intent)
                }

                "goAs" -> {
                    AsVideoActivity.actionStart(context, aggregatingData.dataList[position])
                }

                "goUrl" -> {
                    val uri = Uri.parse(aggregatingData.dataList[position])
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                }

                "getBiliBili" -> {
                    val successToast: String = aggregatingData.successToast[position]
                    val failToast: String = aggregatingData.failToast[position]
                }

                "postBiliBili" -> {
                    val successToast: String = aggregatingData.successToast[position]
                    val failToast: String = aggregatingData.failToast[position]
                    val postData: String = aggregatingData.postData[position]
                    val token = aggregatingData.token[position]

                    postRequestBiliBili(
                        aggregatingData.dataList[position],
                        postData,
                        successToast,
                        failToast,
                        token.toInt(),
                        context
                    )
                }
            }
        }

        private fun postRequestBiliBili(
            url: String,
            postData: String,
            successToast: String,
            failToast: String,
            token: Int,
            context: Context,
        ) {
        }
    }
}
