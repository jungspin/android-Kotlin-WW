package com.cos.weartogo.service


import com.cos.weartogo.data.openweather.WeatherResponse
import com.cos.weartogo.data.weatherCity.WeatherData
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    fun getWeatherAPI(@Query("q") q: String, @Query("appid") appid: String): Call<WeatherData>

//    @GET("weather")
//    fun getWeatherLatLng(@Query("lat") lat: Double,
//                         @Query("lon") lon: Double,
//                         @Query("appid") appid: String,
//                         @Query("lang")lang: String) : Call<WeatherLatLng>

    @GET("weather")
    fun getCurrentWeatherLatLng(@Query("lat") lat: Double,
                         @Query("lon") lon: Double,
                         @Query("appid") appid: String,
                         @Query("lang")lang: String) : Single<WeatherLatLng>

    @GET("forecast")
    fun getForecastLatLng(@Query("lat") lat: Double,
                         @Query("lon") lon: Double,
                         @Query("appid") appid: String,
                         @Query("lang")lang: String) : Single<WeatherResponse>



}