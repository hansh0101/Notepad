package co.kr.notepad.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(): String {
    val pattern = "yyyy-MM-dd HH:mm"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}