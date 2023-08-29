package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvLong
import com.imcys.bilibilias.common.base.extend.mmkvString

object UserInfoRepository : MMKVOwner(mmapID = "data") {
    var cookie by mmkvString("")
    var sessdata by mmkvString("")
    var bili_jct by mmkvString("")
    var mid by mmkvLong(0)
    var as_cookie by mmkvString("")
}
