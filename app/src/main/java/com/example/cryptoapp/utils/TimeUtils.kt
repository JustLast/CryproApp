package com.example.cryptoapp.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun convertTimestampToTime(timestamp: Long?): String {

    if (timestamp == null) return ""

    // The Value to Milliseconds
    val stamp = Timestamp(timestamp * 1000)

    // The Date of value from milliseconds
    val date = Date(stamp.time)

    // The Pattern
    val pattern = "HH:mm:ss"

    // The Simple date format`s object
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    // Get the local time zone to the sdf`s object
    sdf.timeZone = TimeZone.getDefault()

    // Return the date in the required format
    return sdf.format(date)
}