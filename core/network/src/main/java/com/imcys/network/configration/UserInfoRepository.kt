package com.imcys.network.configration

import com.imcys.common.utils.MMKVOwner
import com.imcys.common.utils.mmkvLong

object UserInfoRepository : MMKVOwner(mmapID = "UserInfo") {
    var mid by mmkvLong(-101)
}
