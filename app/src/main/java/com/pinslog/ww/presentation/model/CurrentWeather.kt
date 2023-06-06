package com.pinslog.ww.presentation.model

import com.pinslog.ww.model.WearInfo

/**
* 현재 날씨 데이터
* @author jungspin
* @since 2023/05/31 7:51 AM
*/
data class CurrentWeather(
    val currentTemp: String,
    val currentTime: String,
    val wearInfo: WearInfo,
    val weatherIcon: Int,
    val weatherDescription: String,
)
