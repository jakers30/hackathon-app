package com.raite.studyroom.util

import kotlin.random.Random

/** Short, human-friendly room/invite code (spec section 3.3). */
fun generateInviteCode(length: Int = 6): String {
    val alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // no ambiguous 0/O/1/I
    return buildString { repeat(length) { append(alphabet[Random.nextInt(alphabet.length)]) } }
}

/** Entity type tags used by the outbox/sync worker. */
object Entities {
    const val ROOM = "room"
    const val RESOURCE = "resource"
    const val ROADMAP = "roadmap_item"
    const val REVIEWER = "reviewer"
    const val QUIZ = "quiz"
    const val TASK = "task"
}

/** Outbox operations. */
object Ops {
    const val UPSERT = "UPSERT"
    const val DELETE = "DELETE"
}
