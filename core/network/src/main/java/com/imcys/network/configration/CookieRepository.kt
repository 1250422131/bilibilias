package com.imcys.network.configration

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvLong
import com.imcys.bilibilias.common.base.extend.mmkvString
import com.imcys.common.utils.MMKVOwner
import com.imcys.common.utils.mmkvString

object CookieRepository : MMKVOwner(mmapID = "CookieRepository") {
    var sessionData by mmkvString()
    var bili_jct by mmkvString()
    var DedeUserID by mmkvString()
    var sid by mmkvString()
    var timestamp by mmkvLong()
    val isExpired: Boolean get() = (timestamp < System.currentTimeMillis())

    // 清空cookie
    fun clearCookies() {
        sessionData = null
        bili_jct = null
        DedeUserID = null
        sid = null
        timestamp = 0
    }
}
