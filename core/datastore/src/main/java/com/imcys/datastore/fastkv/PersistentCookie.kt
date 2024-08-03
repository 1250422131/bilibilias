package com.imcys.datastore.fastkv

import javax.inject.*

class PersistentCookie @Inject constructor(): FastKVOwner("PersistentCookie") {
    var refreshToken by string("")
        private set

    fun setRefreshToke(token: String) {
        if (token.isNotBlank()) {
            refreshToken = token
        }
    }

    val SET_COOKIE_SESSDATA = "SESSDATA"
    val SET_COOKIE_BILI_JCT = "bili_jct"
    val SET_COOKIE_DEDEUSERID = "DedeUserID"
    val SET_COOKIE_DEDEUSERID__CKMD5 = "DedeUserID__ckMd5"
    val SET_COOKIE_SID = "sid"
}
