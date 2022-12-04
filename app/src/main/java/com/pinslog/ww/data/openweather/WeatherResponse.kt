package com.pinslog.ww.data.openweather

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<OpenWeather>,
    val message: Int
)