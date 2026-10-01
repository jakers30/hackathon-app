package com.raite.studyroom.ui.navigation

/** Navigation routes (spec section 5.1). */
object Routes {
    const val INTRO = "intro"
    const val AUTH = "auth"
    const val DASHBOARD = "dashboard"
    const val ROOMS = "rooms"
    const val TASKS = "tasks"
    const val SETTINGS = "settings"
    const val CREATE_ROOM = "create_room"

    const val ROOM_ARG = "roomId"
    const val ROOM = "room/{$ROOM_ARG}"

    const val QUIZ_ARG = "quizId"
    const val QUIZ = "quiz/{$QUIZ_ARG}"

    fun room(roomId: String) = "room/$roomId"
    fun quiz(quizId: String) = "quiz/$quizId"
}
