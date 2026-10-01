package com.raite.studyroom.data.remote.ai

import com.raite.studyroom.BuildConfig
import com.raite.studyroom.domain.model.ReviewerSection
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Client for the Ktor AI proxy. The app NEVER holds an AI key (spec section 2):
 * it sends the user's Supabase access token and the room id, and the proxy
 * verifies access server-side before calling the model.
 */
@Singleton
class AiProxyClient @Inject constructor(
    private val http: HttpClient,
    private val supabase: SupabaseClient,
) {
    private val baseUrl = BuildConfig.AI_PROXY_URL.trimEnd('/')

    private fun HttpRequestBuilder.withAuth() {
        val token = supabase.auth.currentSessionOrNull()?.accessToken
        if (token != null) header(HttpHeaders.Authorization, "Bearer $token")
        contentType(ContentType.Application.Json)
    }

    suspend fun rename(request: RenameRequest): String =
        http.post("$baseUrl/ai/rename") {
            withAuth()
            setBody(request)
        }.body<RenameResponse>().displayName

    suspend fun generateRoadmap(request: GenerateRequest): List<RoadmapTopic> =
        http.post("$baseUrl/ai/roadmap") {
            withAuth()
            setBody(request)
        }.body<RoadmapResponse>().items

    suspend fun generateReviewer(request: GenerateRequest): List<ReviewerSection> =
        http.post("$baseUrl/ai/reviewer") {
            withAuth()
            setBody(request)
        }.body<ReviewerResponse>().sections

    suspend fun modify(request: ModifyRequest): ModifyResponse =
        http.post("$baseUrl/ai/modify") {
            withAuth()
            setBody(request)
        }.body()

    suspend fun generateQuiz(request: QuizRequest): QuizResponse =
        http.post("$baseUrl/ai/quiz") {
            withAuth()
            setBody(request)
        }.body()
}
