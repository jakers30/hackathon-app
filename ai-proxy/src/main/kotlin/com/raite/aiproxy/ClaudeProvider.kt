package com.raite.aiproxy

import io.ktor.client.HttpClient
import io.ktor.client.request.header
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

/** Anthropic Claude (spec section 2). PDFs are sent as document blocks. */
class ClaudeProvider(private val http: HttpClient) : AiProvider {

    override suspend fun generate(system: String, parts: List<Part>): String {
        val content = buildJsonArray {
            parts.forEach { part ->
                when (part) {
                    is Part.Text -> add(buildJsonObject {
                        put("type", "text")
                        put("text", part.text)
                    })
                    is Part.Inline -> add(buildJsonObject {
                        put("type", if (part.mimeType == "application/pdf") "document" else "image")
                        put("source", buildJsonObject {
                            put("type", "base64")
                            put("media_type", part.mimeType)
                            put("data", part.base64)
                        })
                    })
                }
            }
        }

        val body = buildJsonObject {
            put("model", Config.anthropicModel)
            put("max_tokens", 4096)
            put("system", system)
            put("messages", buildJsonArray {
                add(buildJsonObject {
                    put("role", "user")
                    put("content", content)
                })
            })
        }

        val response = http.post("https://api.anthropic.com/v1/messages") {
            header("x-api-key", Config.anthropicApiKey)
            header("anthropic-version", "2023-06-01")
            contentType(ContentType.Application.Json)
            setBody(body.toString())
        }.bodyAsText()

        val text = runCatching {
            AiProviders.json.parseToJsonElement(response).jsonObject["content"]!!
                .jsonArray[0].jsonObject["text"]!!.jsonPrimitive.content
        }.getOrNull()

        return text ?: throw IllegalStateException("Claude returned no content: ${response.take(300)}")
    }
}
