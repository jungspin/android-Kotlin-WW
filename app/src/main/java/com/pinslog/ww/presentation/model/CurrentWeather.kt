package com.pinslog.ww.presentation.model

import com.pinslog.ww.model.WearInfo

/**
* 현재 날씨 데이터
* @author jungspin
* @since 2023/05/31 7:51 AM
*/
data class CurrentWeather(
    val currentLocation: String = "",
    val currentTemp: String = "",
    val currentTime: String = "",
    val wearInfo: WearInfo = WearInfo(infoDescription = "", wearingList = arrayListOf()),
    val weatherIcon: Int = -1,
    val weatherDescription: String  = "",
)
