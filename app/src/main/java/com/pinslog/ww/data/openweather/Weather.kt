package com.pinslog.ww.data.openweather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)