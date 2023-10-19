package com.imcys.bilibilias.ui.download

import androidx.compose.runtime.mutableStateMapOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupDownloadProgress @Inject constructor(){
    /**
     * key=cid,value=progress
     */
    val groupTask = mutableStateMapOf<Long, Int>()
}
