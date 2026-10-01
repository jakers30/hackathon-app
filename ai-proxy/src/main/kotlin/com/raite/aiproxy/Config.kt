package com.raite.aiproxy

/**
 * Server-side configuration. Every secret comes from environment variables
 * (spec section 2: the AI key never ships in the app).
 */
object Config {
    val supabaseUrl: String = require("SUPABASE_URL").trimEnd('/')
    val supabaseAnonKey: String = require("SUPABASE_ANON_KEY")
    val serviceRoleKey: String = require("SUPABASE_SERVICE_ROLE_KEY")

    val provider: String = optional("AI_PROVIDER", "gemini").lowercase()
    val geminiApiKey: String = optional("GEMINI_API_KEY", "")
    val geminiModel: String = optional("GEMINI_MODEL", "gemini-1.5-flash")
    val anthropicApiKey: String = optional("ANTHROPIC_API_KEY", "")
    val anthropicModel: String = optional("ANTHROPIC_MODEL", "claude-3-5-sonnet-latest")

    val port: Int = optional("PORT", "8080").toInt()
    val storageBucket: String = optional("STORAGE_BUCKET", "resources")
    val maxFileBytes: Long = 8L * 1024 * 1024

    private fun require(name: String): String =
        System.getenv(name)?.takeIf { it.isNotBlank() }
            ?: error("Missing required environment variable $name")

    private fun optional(name: String, default: String): String =
        System.getenv(name)?.takeIf { it.isNotBlank() } ?: default
}
