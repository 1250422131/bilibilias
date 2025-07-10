package com.imcys.bilibilias.core.data

import co.touchlab.kermit.Logger

internal object TextExtraction {
    private val BVID_REGEX_PATTERN = Regex("BV1[1-9A-HJ-NP-Za-km-z]{9}")

    internal fun textExtract(query: String): MatchResult {
        Logger.d { "query: $query" }
        val result = BVID_REGEX_PATTERN.find(query)
        return if (result != null) {
            MatchResult.BV(result.value)
        } else {
            MatchResult.Error
        }
    }

    internal sealed interface MatchResult {
        data class BV(val id: String) : MatchResult
        data object Error : MatchResult
    }
}

