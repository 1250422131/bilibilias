package com.imcys.bilibilias.data.model

import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserRelationStatInfo
import com.imcys.bilibilias.network.model.user.BILIUserSpaceUpStat

data class BILIUserStatModel(
    val biliUserSpaceUpStat: NetWorkResult<BILIUserSpaceUpStat?>,
    val biliUserRelationStatInfo: NetWorkResult<BILIUserRelationStatInfo?>
)