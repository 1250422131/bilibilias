package com.imcys.bilibilias.base.model.user

import com.imcys.bilibilias.home.ui.model.BangumiPlayBean
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.DashBangumiPlayBean
import com.imcys.bilibilias.home.ui.model.DashVideoPlayBean
import com.imcys.bilibilias.home.ui.model.VideoPageListData
import com.imcys.bilibilias.home.ui.model.VideoPlayBean

data class DownloadTaskDataBean(
    val cid: Long,
    val pageTitle: String,
    val bvid: String,
    // 分辨率
    val qn: String,
    // 视频获取方式选择
    val fnval: String = "80",
    //
    val platform: String = "pc",
    val dashVideoPlayBean: DashVideoPlayBean? = null,
    val dashBangumiPlayBean: DashBangumiPlayBean? = null,
    val bangumiPlayBean: BangumiPlayBean? = null,
    val videoPlayBean: VideoPlayBean? = null,
    val videoPageDataData: VideoPageListData.DataBean? = null,
    val bangumiSeasonBean: BangumiSeasonBean.ResultBean.EpisodesBean? = null,

)
