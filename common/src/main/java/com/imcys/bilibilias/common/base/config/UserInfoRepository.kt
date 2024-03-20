package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvBytes

object UserInfoRepository : MMKVOwner(mmapID = "UserInfoRepository") {
    var asCookies by mmkvBytes(byteArrayOf())
}
