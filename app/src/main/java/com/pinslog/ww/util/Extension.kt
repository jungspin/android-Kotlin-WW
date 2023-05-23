package com.pinslog.ww.util

import java.text.SimpleDateFormat
import java.util.*

const val CURRENT_TIME_PATTERN = "yyyy년 MM월 dd일 E요일 HH시 mm분"

fun Long.toDate(pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale("ko", "KR"))
    val date = Date(this)
    return sdf.format(date).toString()
}
