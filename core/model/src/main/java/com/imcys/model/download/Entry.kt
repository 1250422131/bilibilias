package com.imcys.model.download

import com.imcys.model.video.PageData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Entry(
    @SerialName("audio_quality")
    val audioQuality: Int = 0,
    @SerialName("avid")
    val avid: Long = 0,
    @SerialName("bvid")
    val bvid: String = "",
    @SerialName("cache_version_code")
    val cacheVersionCode: Int = 0,
    @SerialName("can_play_in_advance")
    val canPlayInAdvance: Boolean = false,
    @SerialName("cover")
    val cover: String = "",
    @SerialName("danmaku_count")
    val danmakuCount: Int = 0,
    @SerialName("downloaded_bytes")
    val downloadedBytes: Int = 0,
    @SerialName("guessed_total_bytes")
    val guessedTotalBytes: Int = 0,
    @SerialName("has_dash_audio")
    val hasDashAudio: Boolean = false,
    @SerialName("interrupt_transform_temp_file")
    val interruptTransformTempFile: Boolean = false,
    @SerialName("is_completed")
    val isCompleted: Boolean = false,
    @SerialName("media_type")
    val mediaType: Int = 0,
    @SerialName("owner_avatar")
    val ownerAvatar: String = "",
    @SerialName("owner_id")
    val ownerId: Int = 0,
    @SerialName("owner_name")
    val ownerName: String = "",
    @SerialName("page_data")
    val pageData: PageData = PageData(),
    @SerialName("prefered_video_quality")
    val preferedVideoQuality: Int = 0,
    @SerialName("preferred_audio_quality")
    val preferredAudioQuality: Int = 0,
    @SerialName("quality_pithy_description")
    val qualityPithyDescription: String = "",
    @SerialName("quality_superscript")
    val qualitySuperscript: String = "",
    @SerialName("seasion_id")
    val seasionId: Int = 0,
    @SerialName("spid")
    val spid: Int = 0,
    @SerialName("time_create_stamp")
    val timeCreateStamp: Long = 0,
    @SerialName("time_update_stamp")
    val timeUpdateStamp: Long = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("total_bytes")
    val totalBytes: Int = 0,
    @SerialName("total_time_milli")
    val totalTimeMilli: Int = 0,
    @SerialName("type_tag")
    val typeTag: String = "",
    @SerialName("video_quality")
    val videoQuality: Int = 0
) {
    @Serializable
    data class PageData(
        @SerialName("cid")
        val cid: Long = 0,
        @SerialName("download_subtitle")
        val downloadSubtitle: String = "",
        @SerialName("download_title")
        val downloadTitle: String = "",
        @SerialName("from")
        val from: String = "",
        @SerialName("has_alias")
        val hasAlias: Boolean = false,
        @SerialName("height")
        val height: Int = 0,
        @SerialName("link")
        val link: String = "",
        @SerialName("page")
        val page: Int = 0,
        @SerialName("part")
        val part: String = "",
        @SerialName("rotate")
        val rotate: Int = 0,
        @SerialName("tid")
        val tid: Int = 0,
        @SerialName("vid")
        val vid: String = "",
        @SerialName("width")
        val width: Int = 0
    )
}