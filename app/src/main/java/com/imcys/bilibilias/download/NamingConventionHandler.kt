package com.imcys.bilibilias.download

import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.database.entity.download.FileNamePlaceholder
import com.imcys.bilibilias.database.entity.download.NamingConventionInfo
import com.imcys.bilibilias.database.entity.download.donghuaNamingRules
import com.imcys.bilibilias.database.entity.download.videoNamingRules
import kotlinx.coroutines.flow.first

/**
 * 命名规则处理器
 * 负责根据命名规则生成文件名
 */
class NamingConventionHandler(
    private val appSettingsRepository: AppSettingsRepository
) {
    /**
     * 根据命名规则构建文件名
     */
    suspend fun buildFileName(
        conventionInfo: NamingConventionInfo?,
        fileExtension: String
    ): String {
        val namingRule = when (conventionInfo) {
            is NamingConventionInfo.Video -> {
                appSettingsRepository.appSettingsFlow.first().videoNamingRule
            }
            is NamingConventionInfo.Donghua -> {
                appSettingsRepository.appSettingsFlow.first().bangumiNamingRule
            }
            else -> return "unknown.$fileExtension"
        }

        return buildFileNameWithConvention(namingRule, conventionInfo, fileExtension)
    }

    /**
     * 应用命名规则
     */
    private fun buildFileNameWithConvention(
        namingRule: String,
        conventionInfo: NamingConventionInfo?,
        fileSuffix: String
    ): String {
        var filePath = namingRule

        when (conventionInfo) {
            is NamingConventionInfo.Video -> {
                videoNamingRules.forEach { rule ->
                    val value = when (rule) {
                        FileNamePlaceholder.Video.Aid -> conventionInfo.aid ?: ""
                        FileNamePlaceholder.Video.Author -> conventionInfo.author ?: ""
                        FileNamePlaceholder.Video.BvId -> conventionInfo.bvId ?: ""
                        FileNamePlaceholder.Video.Cid -> conventionInfo.cid ?: ""
                        FileNamePlaceholder.Video.P -> conventionInfo.p ?: ""
                        FileNamePlaceholder.Video.PTitle -> conventionInfo.pTitle ?: ""
                        FileNamePlaceholder.Video.Title -> conventionInfo.title ?: ""
                        FileNamePlaceholder.Video.CollectionSeasonTitle -> conventionInfo.collectionSeasonTitle ?: ""
                        FileNamePlaceholder.Video.CollectionTitle -> conventionInfo.collectionTitle ?: ""
                    }.replace("/", "_")

                    filePath = filePath
                        .replace(rule.placeholder, value)
                        .replace(Regex("_+"), "_")
                        .replace(Regex("_+$"), "")
                }
            }

            is NamingConventionInfo.Donghua -> {
                donghuaNamingRules.forEach { rule ->
                    val value = when (rule) {
                        FileNamePlaceholder.Donghua.Cid -> conventionInfo.cid ?: ""
                        FileNamePlaceholder.Donghua.EpisodeNumber -> conventionInfo.episodeNumber ?: ""
                        FileNamePlaceholder.Donghua.EpisodeTitle -> conventionInfo.episodeTitle ?: ""
                        FileNamePlaceholder.Donghua.Title -> conventionInfo.title ?: ""
                        FileNamePlaceholder.Donghua.SeasonTitle -> conventionInfo.seasonTitle ?: ""
                    }.replace("/", "_")

                    filePath = filePath.replace(rule.placeholder, value)
                }
            }

            else -> {}
        }

        if (!filePath.endsWith(".$fileSuffix")) {
            filePath += ".$fileSuffix"
        }

        return filePath
    }
}
