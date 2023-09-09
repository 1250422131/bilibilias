package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvLong

object UserInfoRepository : MMKVOwner(mmapID = "UserInfo") {
    var mid by mmkvLong()
}
