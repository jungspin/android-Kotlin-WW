package com.pinslog.ww

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import com.google.common.truth.Truth.assertThat
import com.pinslog.ww.model.LatLng
import org.junit.Before
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

const val FAKE_STRING = "WW Application"

@RunWith(MockitoJUnitRunner::class)
class LatLngToAddressTest {

    @Mock
    private lateinit var context: Context

    @Test
    fun readStringFromContext() {
        Mockito.`when`(context.getString(R.string.app_name)).thenReturn(FAKE_STRING)
        val result = context.getString(R.string.app_name)

        assertThat(result).isEqualTo(FAKE_STRING)

    }

    @Test
    fun getAddressTextFromLatLng() {
        val geocoder = Geocoder(context)
        val lat = 35.1560157
        val lng = 129.0594088
        var addressList: List<Address>? = null
        addressList = geocoder.getFromLocation(lat, lng, 10)
        addressList?.let {
            if (addressList.isNotEmpty()) {
                val addr = addressList[0].getAddressLine(0)
                val addrParts = addr.split(" ")
                val address = "${addrParts[2]} ${addrParts[3]}"
                println(address)
            }
        }
    }

    @Test
    fun getLatLngFromAddress() {
        val address = "전포동"
        val geocoder = Geocoder(context)
        val latLngList = geocoder.getFromLocationName(address, 10)
        println(latLngList)
    }

    @Test
    fun dateCheckTest(){
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 E요일 HH시 mm분", Locale("ko", "KR"))
        println(dateFormat.format(date))
    }

    @Test
    fun dateParseDayOfWeekTest(){
        val sdf = SimpleDateFormat("MM월 dd일", Locale.KOREAN)
        //val date = sdf.parse("2023년 3월 13일")

        val cal = Calendar.getInstance()
        cal.time = Date(System.currentTimeMillis())
        println("${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)+1}-${cal.get(Calendar.DATE)}")
        val dayOfWeekString = when (cal.get(Calendar.DAY_OF_WEEK)) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> ""
        }
        println(dayOfWeekString)
    }



}