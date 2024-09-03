package com.kamel.newsapp.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun formatIsoDateToString(isoDate: String): String {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val dateTime = ZonedDateTime.parse(isoDate, formatter)
        "${dateTime.toLocalDate()} ${dateTime.toLocalTime()}"
    } catch (e: Exception) {
        "N/A"
    }
}