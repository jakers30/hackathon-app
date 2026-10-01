package com.raite.studyroom.domain.model

/** Theme preference saved in DataStore (spec section 5.3). */
enum class ThemeMode {
    SYSTEM, LIGHT, DARK;

    companion object {
        fun from(value: String?): ThemeMode =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SYSTEM
    }
}
