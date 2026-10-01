package com.raite.aiproxy

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

/** One piece of the prompt sent to the model. */
sealed interface Part {
    data class Text(val text: String) : Part
    data class Inline(val mimeType: String, val base64: String) : Part
}

interface AiProvider {
    /** Returns the model's raw text answer, which is expected to be JSON. */
    suspend fun generate(system: String, parts: List<Part>): String
}

object AiProviders {
    fun create(http: HttpClient): AiProvider = when (Config.provider) {
        "claude", "anthropic" -> ClaudeProvider(http)
        else -> GeminiProvider(http)
    }

    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
}
