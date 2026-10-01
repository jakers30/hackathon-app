package com.raite.studyroom.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

data class FileMeta(val name: String, val mimeType: String?, val size: Long)

/** Reads display name, MIME type and size for a picked content Uri. */
fun queryFileMeta(context: Context, uri: Uri): FileMeta {
    var name = uri.lastPathSegment?.substringAfterLast('/') ?: "file"
    var size = 0L
    runCatching {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIndex >= 0) cursor.getString(nameIndex)?.let { name = it }
                if (sizeIndex >= 0) size = cursor.getLong(sizeIndex)
            }
        }
    }
    val mime = context.contentResolver.getType(uri)
    return FileMeta(name, mime, size)
}
