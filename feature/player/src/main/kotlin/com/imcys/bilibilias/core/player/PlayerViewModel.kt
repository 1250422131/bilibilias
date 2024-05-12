package com.imcys.bilibilias.core.player

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import javax.inject.Inject

@OptIn(UnstableApi::class)
class PlayerViewModel @Inject constructor(
    val player: AsVideoPlayer,
) : ViewModel() {

}
