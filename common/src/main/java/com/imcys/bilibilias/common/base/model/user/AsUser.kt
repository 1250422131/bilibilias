package com.imcys.bilibilias.common.base.model.user

import com.imcys.bilibilias.common.base.config.CookieRepository

object AsUser {
    var cookie: String = CookieRepository.sessionData!!
    var mid: Long = 0
    var asCookie: String = ""
}