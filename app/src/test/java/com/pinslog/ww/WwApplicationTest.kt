package com.pinslog.ww

import com.pinslog.ww.util.Utility
import org.junit.Test
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
        println(Utility.getRealTemp(temp))
    }

}