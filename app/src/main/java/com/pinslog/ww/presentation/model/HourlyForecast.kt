package com.pinslog.ww.presentation.model

data class HourlyForecast(
    val time: Int,
    val resourceId: Int,
    val temp: Int,
)