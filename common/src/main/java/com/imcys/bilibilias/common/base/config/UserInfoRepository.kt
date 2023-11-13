package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvLong
import com.imcys.bilibilias.common.base.extend.mmkvString
import com.imcys.bilibilias.common.base.extend.mmkvStringSet

object UserInfoRepository : MMKVOwner(mmapID = "UserInfoRepository") {
    var cookie by mmkvString("")
    var mid by mmkvLong(0)

    val asCookies: MutableSet<String>? by mmkvStringSet()
}
