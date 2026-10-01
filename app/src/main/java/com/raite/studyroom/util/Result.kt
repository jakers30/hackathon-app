package com.raite.studyroom.util

/** Minimal result type for repository operations that can fail. */
sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Failure(val message: String, val cause: Throwable? = null) : AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Failure -> this
}

/** Wraps a suspend block, converting thrown exceptions into [AppResult.Failure]. */
suspend fun <T> runCatchingApp(block: suspend () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (t: Throwable) {
        AppResult.Failure(t.message ?: "Something went wrong", t)
    }
