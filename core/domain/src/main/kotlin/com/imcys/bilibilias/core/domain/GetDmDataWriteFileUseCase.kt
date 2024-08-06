package com.imcys.bilibilias.core.domain

import android.content.Context
import com.imcys.bilibilias.core.common.network.di.ApplicationScope
import com.imcys.bilibilias.core.network.repository.DanmakuRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class GetDmDataWriteFileUseCase @Inject constructor(
    private val danmakuRepository: DanmakuRepository,
    @ApplicationContext private val context: Context,
    @ApplicationScope private val scope: CoroutineScope,
) {
    operator fun invoke(cid: Long) {
        scope.launch {
            val byteArray = danmakuRepository.getRealTimeDanmaku(cid)
            File(context.cacheDir.path, "temp_dm.xml").writeBytes(byteArray)
        }
    }
}
