package com.raite.aiproxy

import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

private suspend fun ApplicationCall.user(api: SupabaseApi): String {
    val header = request.headers[HttpHeaders.Authorization] ?: throw UnauthorizedException()
    val token = header.removePrefix("Bearer").trim()
    if (token.isBlank()) throw UnauthorizedException()
    return api.verifyUser(token)
}

fun Route.modifyRoutes(api: SupabaseApi, provider: AiProvider) {
    post("/modify") {
        val userId = call.user(api)
        val body = call.receive<ModifyRequest>()
        if (!api.hasRoomAccess(body.roomId, userId)) throw ForbiddenException()

        val resources = api.resources(body.roomId)
        val json = AiProviders.json
        val parts = Prompts.resourceParts(resources) + listOf(
            Part.Text(
                "Current roadmap: ${json.encodeToString(body.roadmap)}\n" +
                    "Current reviewer: ${json.encodeToString(body.reviewer)}\n" +
                    "Student request: ${body.prompt}\n" +
                    "Return the complete updated roadmap and reviewer, keeping anything the " +
                    "student did not ask to change. Respond as ${Prompts.MODIFY_SCHEMA}"
            )
        )
        val result = json.decodeFromString<ModifyResponse>(
            cleanJson(provider.generate(Prompts.SYSTEM, parts))
        )
        call.respond(result.copy(sections = result.sections.map { it.normalized() }))
    }
}

fun Route.quizRoutes(api: SupabaseApi, provider: AiProvider) {
    post("/quiz") {
        val userId = call.user(api)
        val body = call.receive<QuizRequest>()
        if (!api.hasRoomAccess(body.roomId, userId)) throw ForbiddenException()

        val resources = api.resources(body.roomId)
        if (resources.isEmpty()) {
            return@post call.respond(QuizResponse(title = "Untitled", note = "No resources available."))
        }
        val types = body.questionTypes.joinToString(", ").ifBlank { "multiple_choice" }
        val parts = Prompts.resourceParts(resources) + listOf(
            Part.Text(
                "Create a ${body.type} with ${body.count} questions using these question types: $types. " +
                    "Ground every question strictly in the resources, and set sourceResourceName to the " +
                    "resource the question came from. Include a short explanation for each answer. " +
                    "If the resources are too thin for ${body.count} questions, return fewer and explain " +
                    "why in the 'note' field. " +
                    (if (body.topics.isNotEmpty()) "Focus on: ${body.topics.joinToString(", ")}. " else "") +
                    "Respond as ${Prompts.QUIZ_SCHEMA}"
            )
        )
        val result = AiProviders.json.decodeFromString<QuizResponse>(
            cleanJson(provider.generate(Prompts.SYSTEM, parts))
        )
        call.respond(result)
    }
}
