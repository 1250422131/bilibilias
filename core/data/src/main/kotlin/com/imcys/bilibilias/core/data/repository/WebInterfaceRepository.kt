package com.imcys.bilibilias.core.data.repository

import com.imcys.bilibilias.core.data.Syncable
import com.imcys.bilibilias.core.data.Synchronizer
import com.imcys.bilibilias.core.data.suspendRunCatching
import com.imcys.bilibilias.core.network.repository.LoginRepository
import com.imcys.bilibilias.core.network.utils.TokenUtil
import javax.inject.Inject

class WebInterfaceRepository @Inject constructor(
    private val loginRepository: LoginRepository,
) : Syncable {
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean = suspendRunCatching {
        val nav = loginRepository.nav()
        TokenUtil.setCacheToken(TokenUtil.getMixinKey(nav.imgKey, nav.subKey))
    }.isSuccess
}
