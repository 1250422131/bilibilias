package com.imcys.datastore.mmkv

object UserInfoRepository : MMKVOwner(mmapID = "UserInfo") {
    var mid by mmkvLong(-101)
}
