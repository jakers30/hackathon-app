package com.raite.aiproxy

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/** Google Gemini (spec section 2). Reads PDFs and images inline. */
class GeminiProvider(private val http: HttpClient) : AiProvider {

    override suspend fun generate(system: String, parts: List<Part>): String {
        val body = buildJsonObject {
            put(
                "system_instruction",
                buildJsonObject {
                    put("parts", buildJsonArray { add(buildJsonObject { put("text", system) }) })
                },
            )
            put(
                "contents",
                buildJsonArray {
                    add(
                        buildJsonObject {
                            put("role", "user")
                            put(
                                "parts",
                                buildJsonArray {
                                    parts.forEach { part ->
                                        when (part) {
                                            is Part.Text -> add(buildJsonObject { put("text", part.text) })
                                            is Part.Inline -> add(
                                                buildJsonObject {
                                                    put(
                                                        "inline_data",
                                                        buildJsonObject {
                                                            put("mime_type", part.mimeType)
                                                            put("data", part.base64)
                                                        },
                                                    )
                                                },
                                            )
                                        }
                                    }
                                },
                            )
                        },
                    )
                },
            )
            put(
                "generationConfig",
                buildJsonObject {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.4)
                },
            )
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/" +
            "${Config.geminiModel}:generateContent?key=${Config.geminiApiKey}"

        val response = http.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body.toString())
        }.bodyAsText()

        val text = runCatching {
            AiProviders.json.parseToJsonElement(response).jsonObject["candidates"]!!
                .jsonArray[0].jsonObject["content"]!!.jsonObject["parts"]!!
                .jsonArray[0].jsonObject["text"]!!.jsonPrimitive.content
        }.getOrNull()

        return text ?: throw IllegalStateException("Gemini returned no content: ${response.take(300)}")
    }
}
