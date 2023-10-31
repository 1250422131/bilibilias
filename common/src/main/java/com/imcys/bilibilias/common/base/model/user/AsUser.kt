package com.imcys.bilibilias.common.base.model.user

import com.imcys.network.configration.CookieRepository

object AsUser {
    var cookie: String = com.imcys.network.configration.CookieRepository.sessionData!!
    var mid: Long = 0
    var asCookie: String = ""
}