package com.mb.voiceassistantkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform