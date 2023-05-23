package com.pinslog.ww

import com.pinslog.ww.util.Utility
import org.junit.Test
import java.time.LocalDateTime
import java.util.*

class WwApplicationTest {

    @Test
    fun getDayOfWeekTest(){
        var day = ""
        val cal = Calendar.getInstance()
        // month - 1
        cal.set(2022, 10, 30)
        when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> {
                day = "일"
            }
            2 -> {
                day = "월"
            }
            3 -> {
                day = "화"
            }
            4 -> {
                day = "수"
            }
            5 -> {
                day = "목"
            }
            6 -> {
                day = "금"
            }
            7 -> {
                day = "토"
            }
        }
        println(day)
    }

    @Test
    fun kToCTest(){
        val temp = 274.69
        println(Utility.getRealTempAsString(temp))
    }

    @Test
    fun isBeforeDate() {
        // 이미 지난 시간의 예보는 제외한다.
        // 15:00:00
        val year = "2022".toInt()
        val month = "12".toInt()
        val date = "14".toInt()
        val hour = "12".toInt()

        // 인자를 LocalDateTime으로 만듬
        val localDateTime = LocalDateTime.of(year, month, date, hour, 0)
        // 현재 시간을 LocalDateTime 으로 만듬
        val currentTime = LocalDateTime.now()
        println(localDateTime.isBefore(currentTime))
    }

}