package com.ml.fay.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.getShortMonth(): String? {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, java.util.Locale.getDefault())
}

fun Date.getDayOfMonth(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_MONTH).toString()
}

fun Date.toTimeRangeStringWithTimezone(endDate: Date, timeZone: TimeZone = TimeZone.getDefault()): String {
    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault()).apply {
        this.timeZone = timeZone
    }
    val tzFormatter = SimpleDateFormat("z", Locale.getDefault()).apply {
        this.timeZone = timeZone
    }

    val startTime = timeFormatter.format(this)
    val endTime = timeFormatter.format(endDate)
    val timeZoneAbbreviation = tzFormatter.format(this)
    return "$startTime - $endTime ($timeZoneAbbreviation)"
}