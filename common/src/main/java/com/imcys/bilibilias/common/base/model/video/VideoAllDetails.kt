package com.imcys.bilibilias.common.base.model.video

data class VideoAllDetails(
    val videoDetails: VideoDetails,
    val videoPlayDetails: VideoPlayDetails,
    val isLike: Boolean,
    val isCoins: Boolean,
    val isCollection: Boolean,
)
