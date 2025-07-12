package com.imcys.bilibilias.core.data

import co.touchlab.kermit.Logger

// TODO: query: 2233 DGYSNSYmlsaWJpbGk6Ly92aWRlby8xNzAxMzAyNTU5P3BhZ2U9MCZzb3VyY2VfdHlwZT1oNSZmcm9tX3NwbWlkPW1haW4uaDUubmF0dXJhbC4wLmFpeWlqY3lzZnR6amU0NWJtY3htam85ZCZ1YVNvdXJjZT1jaHJvbWVicm93c2VyJmJzb3VyY2U9c2VhcmNoX2Jpbmcmc3BtaWQ9MzMzLjQwMS5jbGljay52aXNpdEZpcnN0QXdha2VQb3B1cCZ1bmlxdWVfaz0maDVfYnV2aWQ9Q0QzRjhBRjEtNzEzRC01NzkzLTNEOTgtRTg2NjExMDVGOURBOTkxNjNpbmZvYyZoNV9taWQ9JnRzPTE3NTIxNjYyNTEmdWE9TW96aWxsYSUyRjUuMCUyMChMaW51eCUzQiUyMEFuZHJvaWQlMjAxMCUzQiUyMEspJTIwQXBwbGVXZWJLaXQlMkY1MzcuMzYlMjAoS0hUTUwlMkMlMjBsaWtlJTIwR2Vja28pJTIwQ2hyb21lJTJGMTM1LjAuMC4wJTIwTW9iaWxlJTIwU2FmYXJpJTJGNTM3LjM2JTIwRWRnQSUyRjEzNS4wLjAuMCZiaXpfdHlwZT1tc3RhdGlvbiZzZXNzaW9uSWQ9YWl5aWpjeXNmdHpqZTQ1Ym1jeG1qbzlkJml0ZW1faWQ9JTdCJTIyYXZpZCUyMiUzQTE3MDEzMDI1NTklN0Qmb3BlbklkPSZ3eGZpZD0maXNfc3RvcnlfcGxheT0xJnBhZ2VfdHlwZT1uYXR1cmFsJm9wZW5pZD0mcGFnZT0w https://m.bilibili.com
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

