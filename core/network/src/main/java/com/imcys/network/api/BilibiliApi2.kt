package com.imcys.network.api

object BilibiliApi2 {
    // <editor-fold desc="登录">
    const val loginApi = "https://passport.bilibili.com/"

    // 检查 cookie 是否需要刷新
    const val CHECK_COOKIE_REFRESH = loginApi + "x/passport-login/web/cookie/info"

    // 刷新 cookie
    const val COOKIE_REFRESH = loginApi + "x/passport-login/web/cookie/refresh"

    // 让旧 cookie 失效
    const val CONFIRM_REFRESH = loginApi + "x/passport-login/web/confirm/refresh"

    // 刷新 cookie
    const val CORRESPOND = "https://www.bilibili.com/correspond/1/"

    // 申请二维码
    const val getLoginQRPath = loginApi + "x/passport-login/web/qrcode/generate"

    // 需要登陆密钥
    const val getLoginStatePath = loginApi + "x/passport-login/web/qrcode/poll"

    // 注销登录
    const val exitLogin = loginApi + "login/exit/v2"
    // </editor-fold>

    // 获取个人基本信息
    const val getMyUserData = "x/member/web/account"

    // 获取用户卡片信息
    const val getUserCardPath = "x/web-interface/card"

    // post aid bvid like:1,2 csrf:j_xx
    const val likeVideoPath = "x/web-interface/archive/like"

    // 获取视频详细信息 get bvid avid
    const val getVideoDataPath = "x/web-interface/view"
    const val bangumiPlayPath = "pgc/player/web/playurl"

    // 对视频进行点赞
    const val videLikePath = "x/web-interface/archive/like"

    const val videoPlayPath = "x/player/playurl"
    const val VIDEO_PLAY_WBI = "x/player/wbi/playurl"

    // 弹幕下载
    const val videoDanMuPath = "x/v1/dm/list.so"

    // 当前弹幕缓存
    const val thisVideoDanmakuPath = "x/v2/dm/web/seg.so"
    const val thisVideoDanmakuWbiPath = "x/v2/dm/wbi/web/seg.so"

    // 查询视频分P列表
    const val PLAYER_PAGE_LIST = "x/player/pagelist"

    // 修改视频收藏
    const val videoCollectionSetPath = "x/v3/fav/resource/deal"

    // 投币地址
    const val videoCoinAddPath = "x/web-interface/coin/add"

    // 获取剧集明细 get season_id / ep_id
    const val bangumiVideoDataPath = "pgc/view/web/season"

    // 获取用户基本信息
    const val userSpaceDetails = "x/space/wbi/acc/info"

    // 用户状态
    const val userUpStat = "x/space/upstat"

    // 用户投稿
    const val userWorksPath = "x/space/wbi/arc/search"

    // 播放历史
    const val userPlayHistoryPath = "x/web-interface/history/cursor"

    // 追番剧列表
    const val bangumiFollowPath = "x/space/bangumi/follow/list"

    const val Token = "x/web-interface/nav"

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
    const val videoHasLike = "x/web-interface/archive/has/like"
    const val videoHasCoins = "x/web-interface/archive/coins"
    const val videoHasCollection = "x/v2/fav/video/favoured"

    /**
     * 获取视频超详细信息(web端)
     */
    const val VIEW_ULTRA_DETAIL_WBI = "x/web-interface/wbi/view/detail"
    const val VIEW_ULTRA_DETAIL = "x/web-interface/view/detail"

    /**
     * 获取视频详细信息(web端)
     */
    const val VIEW_DETAIL = "x/web-interface/view"

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
}
