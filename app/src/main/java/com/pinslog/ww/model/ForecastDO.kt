package com.pinslog.ww.model

import com.pinslog.ww.util.Utility
import java.util.*

class ForecastDO constructor(
    val month: String,
    val date: String,
    var id: Int,
    var maxTemp: String,
    var minTemp: String,
    val pop: Double,
    val hourlyMap: MutableMap<String, Int>
) {
    private val calendar = Calendar.getInstance()
    val dateString : String
        get() = "$month.$date"
    val resourceId : Int
        get() {
            return Utility.setCodeToImg(id)
        }
    val day: String
        get() {
            calendar.time = Date(System.currentTimeMillis())
            calendar.set(calendar.get(Calendar.YEAR), (month.toInt() - 1), date.toInt())
            return when (calendar.get(Calendar.DAY_OF_WEEK)) {
                1 -> "일"
                2 -> "월"
                3 -> "화"
                4 -> "수"
                5 -> "목"
                6 -> "금"
                7 -> "토"
                else -> ""
            }
        }

    override fun toString(): String {
        return "ForecastDO(month='$month', date='$date', resourceId=$resourceId, maxTemp='$maxTemp', minTemp='$minTemp', day='$day', hour='${hourlyMap.entries.joinToString()})"
    }


}
