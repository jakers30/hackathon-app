package com.raite.aiproxy

import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.decodeFromString

/** Extracts and verifies the Supabase token, returning the user id. */
private suspend fun ApplicationCall.authenticatedUser(api: SupabaseApi): String {
    val header = request.headers[HttpHeaders.Authorization] ?: throw UnauthorizedException()
    val token = header.removePrefix("Bearer").trim()
    if (token.isBlank()) throw UnauthorizedException()
    return api.verifyUser(token)
}

/** Strips markdown fences and returns the first JSON object found. */
fun cleanJson(raw: String): String {
    val text = raw.replace("```json", "").replace("```", "").trim()
    val start = text.indexOf('{')
    val end = text.lastIndexOf('}')
    return if (start >= 0 && end > start) text.substring(start, end + 1) else text
}

fun Route.renameRoutes(api: SupabaseApi, provider: AiProvider) {
    post("/rename") {
        val userId = call.authenticatedUser(api)
        val body = call.receive<RenameRequest>()
        if (!api.hasRoomAccess(body.roomId, userId)) throw ForbiddenException()

        val resources = api.resources(body.roomId)
        val target = resources.firstOrNull { it.id == body.resourceId } ?: resources.firstOrNull()
            ?: return@post call.respond(RenameResponse(body.originalName))

        val parts = Prompts.resourceParts(listOf(target)) + listOf(
            Part.Text(
                "Suggest a short, descriptive study-material name of 3 to 6 words with no " +
                    "special characters, keeping the extension .${body.originalName.substringAfterLast('.', "")}. " +
                    "Respond as ${Prompts.RENAME_SCHEMA}"
            )
        )
        val displayName = runCatching {
            AiProviders.json.decodeFromString<RenameResponse>(cleanJson(provider.generate(Prompts.SYSTEM, parts)))
                .displayName
        }.getOrNull()?.takeIf { it.isNotBlank() } ?: body.originalName

        call.respond(RenameResponse(displayName))
    }
}

fun Route.roadmapRoutes(api: SupabaseApi, provider: AiProvider) {
    post("/roadmap") {
        val userId = call.authenticatedUser(api)
        val body = call.receive<GenerateRequest>()
        if (!api.hasRoomAccess(body.roomId, userId)) throw ForbiddenException()

        val resources = api.resources(body.roomId)
        if (resources.isEmpty()) {
            return@post call.respond(RoadmapResponse())
        }
        val parts = Prompts.resourceParts(resources) + listOf(
            Part.Text(
                "Build a clear study roadmap from these resources." +
                    (body.prompt?.let { " Student request: $it" } ?: "") +
                    " Respond as ${Prompts.ROADMAP_SCHEMA}"
            )
        )
        val response = AiProviders.json.decodeFromString<RoadmapResponse>(
            cleanJson(provider.generate(Prompts.SYSTEM, parts))
        )
        call.respond(response)
    }
}

fun Route.reviewerRoutes(api: SupabaseApi, provider: AiProvider) {
    post("/reviewer") {
        val userId = call.authenticatedUser(api)
        val body = call.receive<GenerateRequest>()
        if (!api.hasRoomAccess(body.roomId, userId)) throw ForbiddenException()

        val resources = api.resources(body.roomId)
        if (resources.isEmpty()) {
            return@post call.respond(ReviewerResponse())
        }
        val parts = Prompts.resourceParts(resources) + listOf(
            Part.Text(
                "Write a study reviewer with organised sections: important concepts, " +
                    "definitions, key points, examples and formulas where applicable. " +
                    "Respond as ${Prompts.REVIEWER_SCHEMA}"
            )
        )
        val response = AiProviders.json.decodeFromString<ReviewerResponse>(
            cleanJson(provider.generate(Prompts.SYSTEM, parts))
        )
        call.respond(response.copy(sections = response.sections.map { it.normalized() }))
    }
}

fun ReviewerSectionDto.normalized(): ReviewerSectionDto = copy(kind = Prompts.normalizeKind(kind))
