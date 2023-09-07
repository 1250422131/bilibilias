package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvLong
import com.imcys.bilibilias.common.base.extend.mmkvString

object CookieManagerRepository : MMKVOwner(mmapID = "CookieManager") {
    var SESSDATA by mmkvString("")
    var bili_jct by mmkvString("")
    var DedeUserID by mmkvString("")
    var DedeUserID__ckMd5 by mmkvString("")
    var sid by mmkvString("")
    var timestamp by mmkvLong()
    val isExpired get() = (timestamp < System.currentTimeMillis()) && SESSDATA.isBlank()
}
