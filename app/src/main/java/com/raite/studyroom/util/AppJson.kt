package com.raite.studyroom.util

import kotlinx.serialization.json.Json

/** Single shared JSON codec for outbox payloads and local caching. */
object AppJson {
    val instance: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }
}
