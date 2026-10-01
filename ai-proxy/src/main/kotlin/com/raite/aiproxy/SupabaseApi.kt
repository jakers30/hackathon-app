package com.raite.aiproxy

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
private data class ResourceRow(
    val id: String,
    @SerialName("original_name") val originalName: String,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("storage_path") val storagePath: String,
)

class UnauthorizedException(message: String = "Invalid or expired session") : Exception(message)
class ForbiddenException(message: String = "You don't have access to this room") : Exception(message)

/**
 * Talks to Supabase with the service-role key. The user's token is verified
 * first, and the room is only read after access is confirmed (spec section 2).
 */
class SupabaseApi(private val http: HttpClient) {

    private val json = Json { ignoreUnknownKeys = true }

    /** Validates the Supabase access token and returns the user id. */
    suspend fun verifyUser(token: String): String {
        val response = http.get("${Config.supabaseUrl}/auth/v1/user") {
            header("apikey", Config.supabaseAnonKey)
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        if (response.status != HttpStatusCode.OK) throw UnauthorizedException()
        val body = response.bodyAsText()
        val id = runCatching { json.parseToJsonElement(body).jsonObject["id"]?.jsonPrimitive?.content }
            .getOrNull()
        return id ?: throw UnauthorizedException()
    }

    /** True when the user is a member or the host of the room. */
    suspend fun hasRoomAccess(roomId: String, userId: String): Boolean {
        val member = serviceGet(
            "/rest/v1/room_members?room_id=eq.$roomId&user_id=eq.$userId&select=id"
        )
        if (member != "[]") return true
        val host = serviceGet("/rest/v1/rooms?id=eq.$roomId&host_id=eq.$userId&select=id")
        return host != "[]"
    }

    suspend fun resources(roomId: String): List<RoomResource> {
        val body = serviceGet(
            "/rest/v1/resources?room_id=eq.$roomId&deleted_at=is.null" +
                "&select=id,original_name,display_name,mime_type,storage_path"
        )
        val rows = runCatching { json.decodeFromString<List<ResourceRow>>(body) }.getOrDefault(emptyList())
        return rows.map { row ->
            val bytes = runCatching { download(row.storagePath) }.getOrNull()
            RoomResource(
                id = row.id,
                name = row.displayName?.takeIf { it.isNotBlank() } ?: row.originalName,
                mimeType = row.mimeType,
                bytes = bytes,
                extractedText = bytes?.let { FileText.extract(it, row.mimeType) },
            )
        }
    }

    suspend fun download(path: String): ByteArray =
        http.get("${Config.supabaseUrl}/storage/v1/object/${Config.storageBucket}/$path") {
            serviceHeaders()
        }.readRawBytes()

    private suspend fun serviceGet(path: String): String =
        http.get(Config.supabaseUrl + path) { serviceHeaders() }.bodyAsText()

    private fun io.ktor.client.request.HttpRequestBuilder.serviceHeaders() {
        header("apikey", Config.serviceRoleKey)
        header(HttpHeaders.Authorization, "Bearer ${Config.serviceRoleKey}")
    }
}
