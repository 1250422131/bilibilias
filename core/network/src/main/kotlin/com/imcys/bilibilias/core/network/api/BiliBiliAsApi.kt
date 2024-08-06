package com.imcys.bilibilias.core.network.api

object BiliBiliAsApi {
    const val SERVICE_TEST_API: String = "http://106.47.27.177:2233/api/v1/"
    const val API = "https://api.misakamoe.com/"
    const val API_HOST = "api.misakamoe.com"
    const val VERSION = 3.5
    const val UPDATE_DATA: String = API + "app/bilibilias.php"
    const val APP_FUNCTION = API + "app/AppFunction.php"
    const val APP_ADD_AS_VIDEO_DATA = API + "bilibili/AppVideoAsAdd.php"

    const val APP_VERSION_DATA_PATH = SERVICE_TEST_API + "versions/$VERSION"
}
