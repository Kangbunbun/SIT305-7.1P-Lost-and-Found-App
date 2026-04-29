package com.example.lostfoundapp.utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
object DateTimeUtils {
    fun getCurrentDateTime(): String {
        val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        return formatter.format(Date())
    }
}