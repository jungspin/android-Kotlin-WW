package com.cos.weartogo.service


import com.cos.weartogo.data.openweather.WeatherResponse
import com.cos.weartogo.data.weatherCity.WeatherData
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Query

interface WeatherAPIService {

    @GET("weather")
    fun getWeatherAPI(@Query("q") q: String, @Query("appid") appid: String): Call<WeatherData>

    @GET("weather")
    fun getWeatherLatLng(@Query("lat") lat: Double,
                         @Query("lon") lon: Double,
                         @Query("appid") appid: String,
                         @Query("lang")lang: String) : Call<WeatherLatLng>




}