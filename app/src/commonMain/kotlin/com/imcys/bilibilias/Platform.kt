package com.imcys.bilibilias

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform