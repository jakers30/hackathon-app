package com.raite.aiproxy

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = Config.port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val http = HttpClient(CIO) {
        expectSuccess = false
        install(ContentNegotiation) { json(AiProviders.json) }
    }
    val api = SupabaseApi(http)
    val provider = AiProviders.create(http)

    install(ServerContentNegotiation) { json(AiProviders.json) }
    install(CallLogging)
    install(StatusPages) {
        exception<UnauthorizedException> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized, ApiError("Invalid or expired session"))
        }
        exception<ForbiddenException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden, ApiError("You don't have access to this room"))
        }
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, ApiError(cause.message ?: "AI request failed"))
        }
    }

    routing {
        get("/health") { call.respondText("ok") }
        route("/ai") {
            renameRoutes(api, provider)
            roadmapRoutes(api, provider)
            reviewerRoutes(api, provider)
            modifyRoutes(api, provider)
            quizRoutes(api, provider)
        }
    }
}
