package com.imcys.bilibilias.network.config

object API {

    object BILIBILI {
        var WEB_API_BASE_URL = "https://api.bilibili.com/"

        const val APP_API_BASE_URL = "https://app.bilibili.com/"

        const val LOGIN_BASE_URL = "https://passport.bilibili.com/"

        const val SPACE_BASE_URL = "https://space.bilibili.com/"

        // 获取登录二维码
        const val WEB_QRCODE_GENERATE_URL = LOGIN_BASE_URL + "x/passport-login/web/qrcode/generate"

        const val TV_QRCODE_GENERATE_URL = LOGIN_BASE_URL + "x/passport-tv-login/qrcode/auth_code"

        // 检测扫码登录状态
        const val WEB_QRCODE_POLL_URL = LOGIN_BASE_URL + "x/passport-login/web/qrcode/poll"

        const val TV_QRCODE_POLL_URL = LOGIN_BASE_URL + "x/passport-tv-login/qrcode/poll"


        // 登录信息
        val WEB_LOGIN_INFO_URL = WEB_API_BASE_URL + "x/web-interface/nav"

        const val TV_LOGIN_INFO_URL = APP_API_BASE_URL + "x/v2/account/myinfo"

        // v3、v4签名：仅Web有需要
        val WEB_SPI_URL = WEB_API_BASE_URL + "x/frontend/finger/spi"

        // 个人主页信息：仅Web有，TV共用
        val WEB_WEBI_ACC_INFO_URL = WEB_API_BASE_URL + "x/space/wbi/acc/info"

        // 稿件播放量和获赞信息，预计仅Web有
        val WEB_SPACE_UPSTAT_URL = WEB_API_BASE_URL + "x/space/upstat"

        // 粉丝数量，关注数量，预计仅Web有
        val WEB_RELATION_STAT_URL = WEB_API_BASE_URL + "x/relation/stat"


        // 投稿列表，仅Web有，TV共用
        val WEB_SPACE_ARC_SEARCH = WEB_API_BASE_URL + "x/space/wbi/arc/search"

        // 视频详情，通用接口，Webi鉴权
        val WEB_WEBI_VIDEO_VIEW = WEB_API_BASE_URL + "x/web-interface/wbi/view"

        // 视频详情，通用接口，非Webi鉴权
        val WEB_WEBI_VIDEO_VIEW_NO_WEBI = WEB_API_BASE_URL + "x/web-interface/view"

        // 番剧详情，通用接口
        val WEB_WEBI_PGC_SEASON_VIEW = WEB_API_BASE_URL + "pgc/view/web/season"

        // 番剧播放信息接口，通用接口
        val WEB_PGC_PLAYER_URL = WEB_API_BASE_URL + "pgc/player/web/playurl"

        // 视频播放信息接口，通用接口
        val WEB_VIDEO_PLAYER_URL = WEB_API_BASE_URL + "x/player/wbi/playurl"

        // 视频播放信息接口，通用接口
        val WEB_VIDEO_PLAYER_NO_WEBI_URL = WEB_API_BASE_URL + "x/player/playurl"

        // 获取追番列表，TV暂未实现
        val WEB_BANGUMI_FOLLOW_URL = WEB_API_BASE_URL + "x/space/bangumi/follow/list"

        // 获取收藏夹列表，TV暂未实现
        val WEB_FOLDER_LIST_URL = WEB_API_BASE_URL + "x/v3/fav/folder/created/list-all"

        // 获取收藏夹内视频，TV暂未实现
        val WEB_FOLDER_FAV_LIST_URL = WEB_API_BASE_URL + "x/v3/fav/resource/list"

        // 获取点赞列表，TV暂未实现
        val WEB_LIKE_LIST_URL = WEB_API_BASE_URL + "x/space/like/video"

        val WEB_COIN_LIST_URL = WEB_API_BASE_URL + "x/space/coin/video"

        val WEB_PLAY_INFO_V2_URL = WEB_API_BASE_URL + "x/player/wbi/v2"

        val WEB_STEIN_EDGE_INFO_V2_URL = WEB_API_BASE_URL + "x/stein/edgeinfo_v2"

        val WEB_HISTORY_CURSOR_URL = WEB_API_BASE_URL + "x/web-interface/history/cursor"

        val WEB_LOGOUT_URL = LOGIN_BASE_URL + "login/exit/v2"

        val WEB_DANMAKU_URL = WEB_API_BASE_URL + "x/v2/dm/wbi/web/seg.so"

    }

    object App {
        const val SSE_HOST = "api.misakamoe.com"
        const val SSE_PORT = 80
        const val SSE_PATH = "/bilibilias/events/subscribe"
        const val BASE_URL = "https://$SSE_HOST/api/v2/"

        const val OLD_BASE_URL = "https://$SSE_HOST/"

        const val OLD_APP_FUNCTION_URL = OLD_BASE_URL + "app/AppFunction.php"

        const val OLD_APP_INFO_URL: String = OLD_BASE_URL + "app/bilibilias.php"

        const val OLD_VIDEO_DATA_POST_URL = OLD_BASE_URL + "bilibili/AppVideoAsAdd.php"
    }
}