package com.imcys.model

import com.imcys.model.video.Archive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![UP主状态数]( * https://github.com/SocialSisterYi/bilibili-API-collect/blob/ef919a61efd0a4813d95585fa0ebd24ce1d2988e/docs/user/status_number.md)
 * ```json
 * {
 * 	"code": 0,
 * 	"message": "0",
 * 	"ttl": 1,
 * 	"data": {
 * 		"archive": {
 * 			"view": 213567370
 * 		},
 * 		"article": {
 * 			"view": 3230808
 * 		},
 * 		"likes": 20295095
 * 	}
 * }
 * ```
 */
@Serializable
data class UpStatBean(
    @SerialName("archive")
    val archive: Archive = Archive(),
    @SerialName("article")
    val article: Article = Article(),
    @SerialName("likes")
    val likes: Int = 0
) {
    @Serializable
    data class Article(
        @SerialName("view")
        val view: Int = 0
    )
}
