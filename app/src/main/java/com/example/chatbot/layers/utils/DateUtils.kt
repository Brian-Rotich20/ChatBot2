package com.example.chatbot.layers.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeDifference(dateString: String): String {
    try {
        // Define the expected format (correcting for 2-digit seconds)
        val formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy:HH:mm:ss")

        // Extract and fix seconds/minutes if they exceed 59
        val parts = dateString.split(":")
        if (parts.size != 6) return "Invalid date format"

        val day = parts[0].toInt()
        val month = parts[1].toInt()
        val year = parts[2].toInt()
        val hour = parts[3].toInt()
        val minute = parts[4].toInt().coerceIn(0, 59)  // Ensure valid range
        val second = parts[5].toInt().coerceIn(0, 59)  // Ensure valid range

        // Reconstruct a valid date string
        val fixedDateString = String.format("%02d:%02d:%04d:%02d:%02d:%02d", day, month, year, hour, minute, second)

        // Parse input date
        val inputDate = LocalDateTime.parse(fixedDateString, formatter)

        // Get the current date and time
        val now = LocalDateTime.now()

        // Calculate the time difference
        val years = ChronoUnit.YEARS.between(inputDate, now)
        val months = ChronoUnit.MONTHS.between(inputDate, now)
        val weeks = ChronoUnit.WEEKS.between(inputDate, now)
        val days = ChronoUnit.DAYS.between(inputDate, now)
        val hours = ChronoUnit.HOURS.between(inputDate, now)
        val minutes = ChronoUnit.MINUTES.between(inputDate, now)

        // Return appropriate format
        return when {
            years >= 1 -> "$years year${if (years > 1) "s" else ""} ago"
            months >= 2 -> "more than 2 months ago"
            months.toInt() == 1 -> "1 month ago"
            weeks >= 1 -> "$weeks week${if (weeks > 1) "s" else ""} ago"
            days >= 1 -> "$days day${if (days > 1) "s" else ""} ago"
            hours >= 1 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes >= 1 -> "${minutes}min${if (minutes > 1)" " else ""} ago"
            else -> "just now"
        }
    } catch (e: Exception) {
        return "Error parsing date"
    }
}
