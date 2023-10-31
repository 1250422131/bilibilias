package com.imcys.bilibilias.common.base.api

/**
 * mid为UID
 */
object BilibiliApi {

    // <editor-fold desc="登录">
    private const val loginApi = "https://passport.bilibili.com/"

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

    // 视频列表
    const val videoPageListPath = "x/player/pagelist"

    // 修改视频收藏
    const val videoCollectionSetPath = "x/v3/fav/resource/deal"

    // 投币地址
    const val videoCoinAddPath = "x/web-interface/coin/add"

    // 获取用户基本信息
    const val userSpaceDetails = "x/space/wbi/acc/info"

    // 用户导航栏信息
    const val userNavDataPath = "x/web-interface/nav"

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

}
