package com.example.racinglog.data.model

object LapTimeUtils {

    fun parseTime(input: String): Long? {
        val text = input.trim()

        val regex = Regex("""^(\d+:)?\d{1,2}\.\d{3}$""")

        if (!regex.matches(text)) return null

        return try {
            val parts = text.split(":")
            val secondsPart = parts.last().split(".")

            val minutes = if (parts.size == 2) {
                parts[0].toLong()
            } else {
                0L
            }

            val seconds = secondsPart[0].toLong()
            val milliseconds = secondsPart[1].toLong()

            if (seconds >= 60) return null

            Math.addExact(
                Math.addExact(
                    Math.multiplyExact(minutes, 60_000L),
                    Math.multiplyExact(seconds, 1000L)
                ),
                milliseconds
            ).takeIf { it > 0L }
        } catch (e: Exception) {
            null
        }
    }

    fun formatTime(timeMs: Long): String {
        val minutes = timeMs / 60_000
        val seconds = (timeMs % 60_000) / 1000
        val milliseconds = timeMs % 1000

        return "%d:%02d.%03d".format(
            minutes,
            seconds,
            milliseconds
        )
    }
}
