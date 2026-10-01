package com.raite.studyroom.data.local

import androidx.room.TypeConverter
import com.raite.studyroom.domain.model.ReviewerSection
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/** Room type converters for list-typed columns (stored as JSON text). */
class Converters {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @TypeConverter
    fun fromStringList(value: List<String>?): String =
        json.encodeToString(ListSerializer(String.serializer()), value ?: emptyList())

    @TypeConverter
    fun toStringList(value: String?): List<String> =
        if (value.isNullOrBlank()) emptyList()
        else runCatching { json.decodeFromString(ListSerializer(String.serializer()), value) }
            .getOrDefault(emptyList())

    @TypeConverter
    fun fromReviewerSections(value: List<ReviewerSection>?): String =
        json.encodeToString(ListSerializer(ReviewerSection.serializer()), value ?: emptyList())

    @TypeConverter
    fun toReviewerSections(value: String?): List<ReviewerSection> =
        if (value.isNullOrBlank()) emptyList()
        else runCatching { json.decodeFromString(ListSerializer(ReviewerSection.serializer()), value) }
            .getOrDefault(emptyList())
}
