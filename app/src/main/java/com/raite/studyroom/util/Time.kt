package com.raite.studyroom.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

/** ISO-8601 UTC timestamp helpers (matches Postgres timestamptz). */
object Time {
    private val iso = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    @Synchronized
    fun nowIso(): String = iso.format(Date())

    fun newId(): String = UUID.randomUUID().toString()
}

/** Short display label, e.g. "Oct 2, 14:30". Falls back to the raw string. */
fun String.toShortDate(): String = runCatching {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = true
    }
    val date = parser.parse(substringBefore('.').removeSuffix("Z"))
    SimpleDateFormat("MMM d, HH:mm", Locale.US).format(date!!)
}.getOrDefault(this)
