package com.raite.aiproxy

import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayInputStream

/**
 * Text extraction for formats the model cannot read directly.
 * PDFs and images are sent to the model as-is (vision/PDF input); DOCX is
 * extracted here with Apache POI (spec section 2).
 */
object FileText {

    fun extract(bytes: ByteArray, mimeType: String): String? = when {
        mimeType.contains("wordprocessingml") -> extractDocx(bytes)
        mimeType.startsWith("text/") -> bytes.toString(Charsets.UTF_8).take(20_000)
        else -> null
    }

    /** True when the file should be sent to the model as an inline attachment. */
    fun isVisionInput(mimeType: String): Boolean =
        mimeType == "application/pdf" || mimeType.startsWith("image/")

    private fun extractDocx(bytes: ByteArray): String? = runCatching {
        XWPFDocument(ByteArrayInputStream(bytes)).use { document ->
            buildString {
                document.paragraphs.forEach { paragraph ->
                    if (paragraph.text.isNotBlank()) appendLine(paragraph.text)
                }
                document.tables.forEach { table ->
                    table.rows.forEach { row ->
                        appendLine(row.tableCells.joinToString(" | ") { it.text })
                    }
                }
            }
        }
    }.getOrNull()
}
