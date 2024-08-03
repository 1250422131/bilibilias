package com.imcys.network.api

object BilibiliApi2 {
    // <editor-fold desc="登录">
    const val loginApi = "https://passport.bilibili.com/"

    // 申请二维码
    const val getLoginQRPath = loginApi + "x/passport-login/web/qrcode/generate"

    // 需要登陆密钥
    const val getLoginStatePath = loginApi + "x/passport-login/web/qrcode/poll"

    // 注销登录
    const val exitLogin = loginApi + "login/exit/v2"
    // </editor-fold>

    // 番剧视频流
    const val PGC_PLAY_URL = "pgc/player/web/v2/playurl"

    // 获取剧集明细 season_id / ep_id
    const val PGC_VIEW_SEASON = "pgc/view/web/season"

    // 追番剧列表
    const val bangumiFollowPath = "x/space/bangumi/follow/list"

    /**
     * 收藏夹
     */
    // 获取收藏夹内容明细列表
    const val getFavoritesContentList = "x/v3/fav/resource/list"

    // 获取收藏夹全部内容
    const val allFavoritesContents = "x/v3/fav/resource/ids"

    // 获取指定用户创建的所有收藏夹信息
    const val userAllFavorites = "x/v3/fav/folder/created/list-all"

    /**
     * 视频点赞&投币&收藏&分享
     */
    const val ARCHIVE_HAS_LIKE = "x/web-interface/archive/has/like"
    const val ARCHIVE_COINS = "x/web-interface/archive/coins"
    const val VIDEO_FAVOURED = "x/v2/fav/video/favoured"

    /**
     * 获取视频超详细信息(web端)
     */
    const val VIEW_ULTRA_DETAIL_WBI = "x/web-interface/wbi/view/detail"
    const val VIEW_ULTRA_DETAIL = "x/web-interface/view/detail"

    /**
     * 获取视频详细信息(web端)
     */
    const val VIEW = "x/web-interface/view"

    /**
     * 获取视频流地址_web端
     */
    const val PLAYER_PLAY_URL_WBI = "x/player/wbi/playurl"

    /**
     * wbi 签名
     */
    const val WBI_SIGNATURE = "x/web-interface/nav"

    /**
     * 查询用户投稿视频明细
     */
    const val WBI_SPACE_ARC_SEARCH = "x/space/wbi/arc/search"

    const val SPACE_CHANNEL_LIST = "x/space/channel/list"
    const val SPACE_CHANNEL_VIDEO = "x/space/channel/video"
    const val SPACE_SEASONS_SERIES_LIST = "/x/polymer/web-space/seasons_series_list"
    const val SPACE_SEASONS_ARCHIVES_LIST = "/x/polymer/web-space/seasons_archives_list"
    const val SERIES_SERIES = "/x/series/series"
}
