package com.imcys.bilibilias.common.base.config

import com.imcys.bilibilias.common.base.extend.MMKVOwner
import com.imcys.bilibilias.common.base.extend.mmkvBool
import com.imcys.bilibilias.common.base.extend.mmkvBytes
import com.imcys.bilibilias.common.base.extend.mmkvLong
import com.imcys.bilibilias.common.base.extend.mmkvString

object UserInfoRepository : MMKVOwner(mmapID = "UserInfoRepository") {
    var cookie by mmkvString("")
    var mid by mmkvLong(0)

    var asCookies by mmkvBytes(byteArrayOf())
}


object TvUserInfoRepository : MMKVOwner(mmapID = "TvUserInfoRepository") {
    var mid by mmkvLong(0)
    var accessToken by mmkvString("")
    var refreshToken by mmkvString("")
    var enabledRoaming by mmkvBool(false)

}
