package com.example.chatbot.layers.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

class NumberFormattingTransformation : VisualTransformation {
    private val formatter = DecimalFormat("#,###")

    override fun filter(text: AnnotatedString): TransformedText {
        val formattedText = formatNumber(text.text)
        val offsetMapping = createOffsetMapping(text.text, formattedText)
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }

    // ... (formatNumber method remains unchanged)

    private fun formatNumber(numberString: String): String {
        val cleanNumberString = numberString.replace(",", "").filter { it.isDigit() }
        return formatter.format(cleanNumberString.toLong())
    }

    private fun createOffsetMapping(originalText: String, formattedText: String): OffsetMapping {
        val mapping = mutableMapOf<Int, Int>()
        var formattedIndex = 0

        for (originalIndex in originalText.indices) {
            val char = originalText[originalIndex]
            if (char.isDigit()) {
                mapping[originalIndex] = formattedIndex
                formattedIndex++ // Account for commas
            } else {
                // Handle non-numeric characters as needed
                // Example: mapping[originalIndex] = formattedIndex++ to insert as-is
            }
        }

        return object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return mapping[offset] ?: offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Validate offset to prevent out-of-bounds errors
                return mapping.entries.firstOrNull { it.value == offset }?.key
                    ?: minOf(offset, originalText.length)
            }
        }
    }
}