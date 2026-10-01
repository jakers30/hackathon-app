package com.raite.aiproxy

import java.util.Base64

/** System prompts and JSON schemas (spec sections 3.5, 3.6 and 9). */
object Prompts {

    val SYSTEM = """
You are a study assistant. Use only the provided room resources and the
current study materials. Stay on educational topics. Refuse offensive or
harmful requests. Do not complete graded assignments or exams for the student.
If the resources do not cover something, say so instead of inventing it.
Respond in the JSON format requested and nothing else.
""".trim()

    val ROADMAP_SCHEMA = """
{"items":[{"topic":"string","description":"string","subtopics":["string"]}]}
""".trim()

    val REVIEWER_SCHEMA = """
{"sections":[{"title":"string","kind":"CONCEPTS|DEFINITIONS|KEY_POINTS|EXAMPLES|FORMULAS|NOTES","content":["string"]}]}
""".trim()

    val MODIFY_SCHEMA = """
{"items":[{"topic":"string","description":"string","subtopics":["string"]}],
 "sections":[{"title":"string","kind":"CONCEPTS|DEFINITIONS|KEY_POINTS|EXAMPLES|FORMULAS|NOTES","content":["string"]}]}
""".trim()

    val QUIZ_SCHEMA = """
{"title":"string","questions":[{"type":"multiple_choice|true_false|identification|short_answer",
"prompt":"string","options":["string"],"correctAnswer":"string","explanation":"string",
"sourceResourceName":"string"}],"note":"optional string when fewer questions were possible"}
""".trim()

    const val RENAME_SCHEMA = """{"displayName":"string"}"""

    fun resourceParts(resources: List<RoomResource>): List<Part> {
        val parts = mutableListOf<Part>()
        val names = resources.joinToString(", ") { it.name }
        parts += Part.Text("Room resources: $names")
        resources.forEach { resource ->
            when {
                FileText.isVisionInput(resource.mimeType) && resource.bytes != null -> {
                    val base64 = Base64.getEncoder().encodeToString(resource.bytes)
                    parts += Part.Text("Resource file: ${resource.name}")
                    parts += Part.Inline(resource.mimeType, base64)
                }
                resource.extractedText != null -> {
                    parts += Part.Text("Resource '${resource.name}' text:\n${resource.extractedText}")
                }
                else -> parts += Part.Text("Resource '${resource.name}' could not be read; skip it.")
            }
        }
        return parts
    }

    /** Maps a model-supplied kind onto the enum names the app expects. */
    fun normalizeKind(kind: String?): String = when (kind?.lowercase()?.replace("-", "_")?.replace(" ", "_")) {
        "concepts", "concept" -> "CONCEPTS"
        "definitions", "definition" -> "DEFINITIONS"
        "key_points", "keypoints", "key_point" -> "KEY_POINTS"
        "examples", "example" -> "EXAMPLES"
        "formulas", "formula" -> "FORMULAS"
        else -> "NOTES"
    }
}
