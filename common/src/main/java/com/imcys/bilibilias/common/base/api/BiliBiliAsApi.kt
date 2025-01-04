package com.imcys.bilibilias.common.base.api

object BiliBiliAsApi {

    const val serviceTestApi: String = "http://106.47.27.177:2233/api/v1/"
    private const val serviceApi: String = "https://api.misakamoe.com/"
    const val version = 4.2
    const val updateDataPath: String = serviceApi + "app/bilibilias.php"
    const val appFunction = serviceApi + "app/AppFunction.php"
    const val appAddAsVideoData = serviceApi + "bilibili/AppVideoAsAdd.php"

    const val appVersionDataPath = serviceTestApi + "versions/$version"
}
