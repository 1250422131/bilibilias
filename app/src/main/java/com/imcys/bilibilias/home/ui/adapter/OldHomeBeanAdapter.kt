package com.imcys.bilibilias.home.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.model.OldHomeBannerDataBean
import com.imcys.bilibilias.databinding.ItemHomeBannerBinding
import com.imcys.bilibilias.home.ui.activity.AsVideoActivity
import com.youth.banner.adapter.BannerAdapter

class OldHomeBeanAdapter(
    datas: MutableList<String>,
    private val sumData: OldHomeBannerDataBean,
) : BannerAdapter<String, ViewHolder>(datas) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ItemHomeBannerBinding>(
                LayoutInflater.from(parent?.context),
                R.layout.item_home_banner,
                parent,
                false
            )
        return ViewHolder(binding.root)
    }

    @SuppressLint("CheckResult")
    override fun onBindView(holder: ViewHolder?, data: String?, position: Int, size: Int) {
        val binding = DataBindingUtil.getBinding<ItemHomeBannerBinding>(holder!!.itemView)?.apply {
            itemHomeBannerTitle.text = data
            Glide.with(itemHomeBannerImage.context).load(sumData.imgUrlList[position])
                .into(itemHomeBannerImage)

            holder.itemView.setOnClickListener {
                loadEvent(sumData.typeList[position], position, holder.itemView.context)
            }
        }
    }

    @SuppressLint("IntentReset")
    private fun loadEvent(s: String?, position: Int, context: Context) {
        var intent = Intent()
        when (s) {
            "goBilibili" -> {
                intent.type = "text/plain"
                intent.data = Uri.parse(sumData.dataList[position])
                intent.action = "android.intent.action.VIEW"
                context.startActivity(intent)
            }

            "goAs" -> {
                AsVideoActivity.actionStart(context, sumData.dataList[position])
            }

            "goUrl" -> {
                val uri = Uri.parse(sumData.dataList[position])
                intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }

            "getBiliBili" -> {
                val successToast: String = sumData.successToast[position]
                val failToast: String = sumData.failToast[position]
                getRequestBiliBili(sumData.dataList[position], successToast, failToast, context)
            }

            "postBiliBili" -> {
                val successToast: String = sumData.successToast[position]
                val failToast: String = sumData.failToast[position]
                val postData: String = sumData.postData[position]
                val token = sumData.token[position]

                postRequestBiliBili(
                    sumData.dataList[position],
                    postData,
                    successToast,
                    failToast,
                    token,
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

    private fun getRequestBiliBili(
        url: String,
        successToast: String,
        failToast: String,
        context: Context,
    ) {
    }
}
