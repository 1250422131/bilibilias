package com.imcys.bilibilias.base.model.user

import com.imcys.bilibilias.common.base.model.BangumiPlayBean
import com.imcys.bilibilias.home.ui.model.DashBangumiPlayBean
import com.imcys.bilibilias.common.base.model.video.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.video.VideoPageListData
import com.imcys.bilibilias.common.base.model.video.VideoPlayDetails

data class DownloadTaskDataBean(
    val cid: Long,
    val pageTitle: String,
    val bvid: String,
    // 分辨率
    val qn: String,
    // 视频获取方式选择
    val fnval: String = "80",
    val platform: String = "pc",
    val dashVideoPlayBean: DashVideoPlayBean? = null,
    val dashBangumiPlayBean: DashBangumiPlayBean? = null,
    val bangumiPlayBean: BangumiPlayBean? = null,
    val videoPlayDetails: VideoPlayDetails? = null,
    val videoPageDataData: VideoPageListData? = null,
    val bangumiSeasonBean: Bangumi.Result.Episode? = null,
    )
