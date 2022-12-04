package com.pinslog.ww.data.openweather

data class OpenWeather(
    val dt: String,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: Sys,
    val dt_txt: String,
    var date: String
)
