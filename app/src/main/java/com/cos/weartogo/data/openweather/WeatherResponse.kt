package com.cos.weartogo.data.openweather

import com.google.gson.JsonArray

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<OpenWeather>,
    val message: Int
)