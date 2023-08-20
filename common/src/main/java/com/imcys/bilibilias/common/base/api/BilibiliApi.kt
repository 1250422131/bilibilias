package com.imcys.bilibilias.common.base.api

object BilibiliApi {

    /**
     * 常见注解
     * mid为UID
     *
     */

    private const val serviceApi: String = "https://api.bilibili.com/"
    const val loginApi: String = "https://passport.bilibili.com/"
    var roamApi: String = "https://api.bilibili.com/"
    private const val liveRoomApi = "https://api.live.bilibili.com/"

    const val getLoginQRPath: String = loginApi + "x/passport-login/web/qrcode/generate"

    // 需要登陆密钥
    const val getLoginStatePath: String = loginApi + "x/passport-login/web/qrcode/poll"

    // 需要传入PS为展示项
    const val homeRCMDVideoPath: String = serviceApi + "x/web-interface/index/top/feed/rcmd"

    // 热门视频
    // get，ps和pn
    const val homePopularVideoPath: String = serviceApi + "x/web-interface/popular"

    // 需要传入mid
    const val getUserInfoPath: String = serviceApi + "x/space/wbi/acc/info"

    // 获取个人基本信息
    const val getMyUserData = serviceApi + "x/member/web/account"

    // 获取用户卡片信息
    const val getUserCardPath = serviceApi + "x/web-interface/card"

    // post aid bvid like:1,2 csrf:j_xx
    const val likeVideoPath = serviceApi + "x/web-interface/archive/like"

    // 获取视频详细信息 get bvid avid
    const val getVideoDataPath = serviceApi + "x/web-interface/view"

    // 对视频进行点赞
    const val videLikePath = serviceApi + "x/web-interface/archive/like"

    // 对视频进行投币
    const val videAddCoinPath = serviceApi + "x/web-interface/coin/add"

    // 获取收藏列表
    const val userCreatedScFolderPath = serviceApi + "x/v3/fav/folder/created/list-all"

    // 收藏夹详细内容
    const val userCollectionDataPath = serviceApi + "x/v3/fav/resource/list"

    const val videoPlayPath = serviceApi + "x/player/playurl"
    var bangumiPlayPath = roamApi + "pgc/player/web/playurl"

    // 弹幕下载
    const val videoDanMuPath = serviceApi + "x/v1/dm/list.so"

    // 当前弹幕缓存
    const val thisVideoDanmakuPath = "${serviceApi}x/v2/dm/web/seg.so"

    // 视频列表
    const val videoPageListPath = serviceApi + "x/player/pagelist"

    // 修改视频收藏
    const val videoCollectionSetPath = serviceApi + "x/v3/fav/resource/deal"

    // 投币地址
    const val videoCoinAddPath = serviceApi + "x/web-interface/coin/add"

    // 获取剧集明细 get season_id / ep_id
    var bangumiVideoDataPath = roamApi + "pgc/view/web/season"

    // 获取用户基本信息
    const val userBaseDataPath = serviceApi + "x/space/wbi/acc/info"

    // 用户导航栏信息
    const val userNavDataPath = serviceApi + "x/web-interface/nav"

    // 用户状态
    const val userUpStat = serviceApi + "x/space/upstat"

    // 用户投稿
    const val userWorksPath = serviceApi + "x/space/wbi/arc/search"

    // 播放历史
    const val userPlayHistoryPath = serviceApi + "x/web-interface/history/cursor"

    // 注销登录
    const val exitLogin = loginApi + "login/exit/v2"

    // 点赞判断
    const val archiveHasLikePath = serviceApi + "x/web-interface/archive/has/like"

    // 投币判断
    const val archiveCoinsPath = serviceApi + "x/web-interface/archive/coins"

    // 收藏判断
    const val archiveFavoured = serviceApi + "x/v2/fav/video/favoured"

    // 追番剧列表
    const val bangumiFollowPath = serviceApi + "x/space/bangumi/follow/list"

    // 直播间信息
    const val liveRoomDataPath = liveRoomApi + "room/v1/Room/get_info"

    // 直播用户信息
    const val liveUserMasterInfo = liveRoomApi + "live_user/v1/Master/info"

    // 直播间播放数据信息
    const val roomPlayInfo = liveRoomApi + "xlive/web-room/v2/index/getRoomPlayInfo"

    const val liveRoomPlayUrl = liveRoomApi + "room/v1/Room/playUrl"
}
