package com.cos.weartogo.data.openweather

data class OpenWeather(
    val dt: String,
    val main: Main,
    val weather: Weather,
    val wind: Wind,
    val visibility: Int,
    val pop: Int,
    val sys: Sys,
    val dt_txt: String
)
