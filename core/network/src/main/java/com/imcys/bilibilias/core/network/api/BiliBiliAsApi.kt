package com.imcys.bilibilias.core.network.api

object BiliBiliAsApi {
    const val serviceTestApi: String = "http://106.47.27.177:2233/api/v1/"
    const val API: String = "https://api.misakamoe.com/"
    const val VERSION = 3.5
    const val UPDATE_DATA: String = API + "app/bilibilias.php"
    const val appFunction = API + "app/AppFunction.php"
    const val appAddAsVideoData = API + "bilibili/AppVideoAsAdd.php"

    const val appVersionDataPath = serviceTestApi + "versions/$VERSION"
}
