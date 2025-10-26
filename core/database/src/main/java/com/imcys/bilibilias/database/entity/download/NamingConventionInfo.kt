package com.imcys.bilibilias.database.entity.download

val videoNamingRules = listOf(
    FileNamePlaceholder.Video.Title,
    FileNamePlaceholder.Video.PTitle,
    FileNamePlaceholder.Video.Author,
    FileNamePlaceholder.Video.P,
    FileNamePlaceholder.Video.Aid,
    FileNamePlaceholder.Video.BvId,
    FileNamePlaceholder.Video.Cid,
)

val donghuaNamingRules = listOf(
    FileNamePlaceholder.Donghua.Title,
    FileNamePlaceholder.Donghua.EpisodeTitle,
    FileNamePlaceholder.Donghua.EpisodeNumber,
    FileNamePlaceholder.Donghua.Cid,
)


sealed class FileNamePlaceholder(
    open val placeholder: String,
    open val description: String,
) {
    sealed class Video(
        override val placeholder: String,
        override val description: String,
    ) : FileNamePlaceholder(
        placeholder,
        description,
    ) {
        object Title : Video("{title}", "视频标题")
        object PTitle : Video("{p_title}", "分P标题")
        object Author : Video("{author}", "视频作者")
        object BvId : Video("{bvid}", "BV号")
        object Aid : Video("{aid}", "AV号")
        object Cid : Video("{cid}", "CID号")
        object P : Video("{p}", "分P序号")
    }

    sealed class Donghua(
        override val placeholder: String,
        override val description: String,
    ) : FileNamePlaceholder(placeholder, description) {
        object Title : Donghua("{title}", "动画标题")
        object EpisodeTitle : Donghua("{episode_title}", "动画集标题")
        object EpisodeNumber : Donghua("{episode_number}", "动画集数")
        object Cid : Donghua("{cid}", "CID号")
    }

}

sealed interface NamingConventionInfo {
    data class Video(
        var title: String? = null,
        var pTitle: String? = null,
        var author: String? = null,
        var bvId: String? = null,
        var aid: String? = null,
        var cid: String? = null,
        var p: String? = null,
    ) : NamingConventionInfo

    data class Donghua(
        var title: String? = null,
        var episodeTitle: String? = null,
        var episodeNumber: String? = null,
        var cid: String? = null,
    ) : NamingConventionInfo
}