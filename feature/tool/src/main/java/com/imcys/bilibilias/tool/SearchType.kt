package com.imcys.bilibilias.tool

sealed interface SearchType {
    data object None : SearchType
    data class BV(val id: String) : SearchType
    data class AV(val id: String) : SearchType
    data class EP(val id: String) : SearchType
    data class ShortLink(val url: String) : SearchType
}