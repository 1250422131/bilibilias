package com.imcys.bilibilias.tool_livestream.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class RoomPlayUrlInfoBean(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // 0
    @SerializedName("ttl")
    val ttl: Int, // 1
) : Serializable {
    data class Data(
        @SerializedName("accept_quality")
        val acceptQuality: List<String>,
        @SerializedName("current_qn")
        val currentQn: Int, // 150
        @SerializedName("current_quality")
        val currentQuality: Int, // 3
        @SerializedName("durl")
        val durl: List<Durl>,
        @SerializedName("quality_description")
        val qualityDescription: List<QualityDescription>,
    ) : Serializable {
        data class Durl(
            @SerializedName("length")
            val length: Int, // 0
            @SerializedName("order")
            val order: Int, // 1
            @SerializedName("p2p_type")
            val p2pType: Int, // 0
            @SerializedName("stream_type")
            val streamType: Int, // 0
            @SerializedName("url")
            val url: String, // https://cn-sxxa-cu-02-06.bilivideo.com/live-bvc/157713/live_474267806_49497952_1500.m3u8?expires=1674633778&len=0&oi=3026178251&pt=h5&qn=0&trid=100389a5429fcefc43fbad7831e55bd3a2da&sigparams=cdn,expires,len,oi,pt,qn,trid&cdn=cn-gotcha01&sign=6a3579ac513ae32d7b769bba3a3b0a6d&sk=4207df3de646838b084f14f252be3affacddc1131b55556afe312ac1012d0357&p2p_type=0&src=57345&sl=1&free_type=0&sid=cn-sxxa-cu-02-06&chash=1&sche=ban&score=15&pp=rtmp&source=one&trace=0&site=7f60134af58ba6b53b9ebba68984d44d&order=1
        ) : Serializable

        data class QualityDescription(
            @SerializedName("desc")
            val desc: String, // 原画
            @SerializedName("qn")
            val qn: Int, // 10000
        ) : Serializable
    }
}