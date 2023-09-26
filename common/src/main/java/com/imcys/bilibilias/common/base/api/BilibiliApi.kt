package com.imcys.bilibilias.common.base.api

import com.imcys.bilibilias.common.base.constant.ROAM_API

object BilibiliApi {

    /**
     * 常见注解
     * mid为UID
     */
    const val loginApi = "https://passport.bilibili.com/"

    private const val liveRoomApi = "https://api.live.bilibili.com/"

    const val getLoginQRPath = loginApi + "x/passport-login/web/qrcode/generate"

    // 需要登陆密钥
    const val getLoginStatePath = loginApi + "x/passport-login/web/qrcode/poll"

    // 需要传入PS为展示项
    const val homeRCMDVideoPath = ROAM_API + "x/web-interface/index/top/feed/rcmd"

    // 热门视频
    // get，ps和pn
    const val homePopularVideoPath = ROAM_API + "x/web-interface/popular"

    // 需要传入mid
    const val getUserInfoPath = ROAM_API + "x/space/wbi/acc/info"

    // 获取个人基本信息
    const val getMyUserData = ROAM_API + "x/member/web/account"

    // 获取用户卡片信息
    const val getUserCardPath = ROAM_API + "x/web-interface/card"

    // post aid bvid like:1,2 csrf:j_xx
    const val likeVideoPath = ROAM_API + "x/web-interface/archive/like"

    // 获取视频详细信息 get bvid avid
    const val getVideoDataPath = ROAM_API + "x/web-interface/view"

    // 对视频进行点赞
    const val videLikePath = ROAM_API + "x/web-interface/archive/like"

    //视频V2接口信息->名称暂定 要求AID CID同时存在
    const val videoInfoV2 = "x/player/wbi/v2"

    // 对视频进行投币
    const val videAddCoinPath = ROAM_API + "x/web-interface/coin/add"

    // 获取收藏列表
    const val userCreatedScFolderPath = ROAM_API + "x/v3/fav/folder/created/list-all"

    // 收藏夹详细内容
    const val userCollectionDataPath = ROAM_API + "x/v3/fav/resource/list"

    const val videoPlayPath = ROAM_API + "x/player/playurl"
    var bangumiPlayPath = ROAM_API + "pgc/player/web/playurl"

    // 弹幕下载
    const val videoDanMuPath = ROAM_API + "x/v1/dm/list.so"

    // 当前弹幕缓存
    const val thisVideoDanmakuPath = ROAM_API + "x/v2/dm/web/seg.so"


    // 视频列表
    const val videoPageListPath = ROAM_API + "x/player/pagelist"

    // 修改视频收藏
    const val videoCollectionSetPath = ROAM_API + "x/v3/fav/resource/deal"

    // 投币地址
    const val videoCoinAddPath = ROAM_API + "x/web-interface/coin/add"

    // 获取剧集明细 get season_id / ep_id
    var bangumiVideoDataPath = ROAM_API + "pgc/view/web/season"

    // 获取用户基本信息
    const val userBaseDataPath = ROAM_API + "x/space/wbi/acc/info"

    // 用户导航栏信息
    const val userNavDataPath = ROAM_API + "x/web-interface/nav"

    // 用户状态
    const val userUpStat = ROAM_API + "x/space/upstat"

    // 用户投稿
    const val userWorksPath = ROAM_API + "x/space/wbi/arc/search"

    // 播放历史
    const val userPlayHistoryPath = ROAM_API + "x/web-interface/history/cursor"

    // 注销登录
    const val exitLogin = loginApi + "login/exit/v2"

    // 点赞判断
    const val archiveHasLikePath = ROAM_API + "x/web-interface/archive/has/like"

    // 投币判断
    const val archiveCoinsPath = ROAM_API + "x/web-interface/archive/coins"

    // 收藏判断
    const val archiveFavoured = ROAM_API + "x/v2/fav/video/favoured"

    // 追番剧列表
    const val bangumiFollowPath = ROAM_API + "x/space/bangumi/follow/list"

    // 直播间信息
    const val liveRoomDataPath = liveRoomApi + "room/v1/Room/get_info"

    // 直播用户信息
    const val liveUserMasterInfo = liveRoomApi + "live_user/v1/Master/info"

    // 直播间播放数据信息
    const val roomPlayInfo = liveRoomApi + "xlive/web-room/v2/index/getRoomPlayInfo"

    const val liveRoomPlayUrl = liveRoomApi + "room/v1/Room/playUrl"
}
