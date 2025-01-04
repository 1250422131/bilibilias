package com.imcys.bilibilias.common.base.api

object BilibiliApi {

    /**
     * 常见注解
     * mid为UID
     */
    const val loginApi = "https://passport.bilibili.com/"

    const val spacePath = "https://space.bilibili.com/"

    const val getLoginQRPath = loginApi + "x/passport-login/web/qrcode/generate"

    // 需要登陆密钥
    const val getLoginStatePath = loginApi + "x/passport-login/web/qrcode/poll"

    // 需要传入mid
    const val getUserInfoPath = "x/space/wbi/acc/info"

    // 获取个人基本信息
    const val getMyUserData = "x/member/web/account"

    // 获取用户卡片信息
    const val getUserCardPath = "x/web-interface/card"

    // 获取视频详细信息 get bvid avid
    const val getVideoDataPath = "x/web-interface/view"

    // 对视频进行点赞
    const val videLikePath = "x/web-interface/archive/like"

    // 视频V2接口信息->名称暂定 要求AID CID同时存在
    const val videoInfoV2 = "x/player/wbi/v2"

    // 获取收藏列表
    const val userCreatedScFolderPath = "x/v3/fav/folder/created/list-all"

    // 收藏夹详细内容
    const val userCollectionDataPath = "x/v3/fav/resource/list"

    const val videoPlayPath = "x/player/playurl"
    const val bangumiPlayPath = "pgc/player/web/playurl"

    // 弹幕下载
    const val videoDanMuPath = "x/v1/dm/list.so"

    // 视频列表
    const val videoPageListPath = "x/player/pagelist"

    // 修改视频收藏
    const val videoCollectionSetPath = "x/v3/fav/resource/deal"

    // 投币地址
    const val videoCoinAddPath = "x/web-interface/coin/add"

    // 获取剧集明细 get season_id / ep_id
    const val bangumiVideoDataPath = "pgc/view/web/season"

    // 获取用户基本信息
    const val userBaseDataPath = getUserInfoPath

    // 用户导航栏信息
    const val userNavDataPath = "x/web-interface/nav"

    // 用户状态
    const val userUpStat = "x/space/upstat"

    // 用户投稿
    const val userWorksPath = "x/space/wbi/arc/search"

    // 播放历史
    const val userPlayHistoryPath = "x/web-interface/history/cursor"

    // 注销登录
    const val exitLogin = loginApi + "login/exit/v2"

    // 点赞判断
    const val archiveHasLikePath = "x/web-interface/archive/has/like"

    // 投币判断
    const val archiveCoinsPath = "x/web-interface/archive/coins"

    // 收藏判断
    const val archiveFavoured = "x/v2/fav/video/favoured"

    // 追番剧列表
    const val bangumiFollowPath = "x/space/bangumi/follow/list"
}
