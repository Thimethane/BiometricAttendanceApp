package com.timothee.biometricattendance.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun getCurrentDate(): String {
        val format = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        return format.format(Date())
    }

    fun getCurrentTime(): String {
        val format = SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault())
        return format.format(Date())
    }

    fun formatTimestamp(timestamp: Long): String {
        val format = SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault())
        return format.format(Date(timestamp))
    }

    fun formatDate(timestamp: Long): String {
        val format = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        return format.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        val format = SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.getDefault())
        return format.format(Date(timestamp))
    }

    fun calculateDuration(startTime: Long, endTime: Long): String {
        val durationMillis = endTime - startTime
        val hours = durationMillis / (1000 * 60 * 60)
        val minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60)
        return String.format("%dh %dm", hours, minutes)
    }
}