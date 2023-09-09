package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvLong
import com.imcys.bilibilias.common.base.extend.mmkvString

object CookieRepository : MMKVOwner(mmapID = "CookieRepository") {
    var sessionData by mmkvString()
    var bili_jct by mmkvString()
    var DedeUserID by mmkvString()
    var sid by mmkvString()
    var timestamp by mmkvLong()
    val isExpired get() = (timestamp < System.currentTimeMillis()) && sessionData == null
}
