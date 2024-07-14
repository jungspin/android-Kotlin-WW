package com.pinslog.ww.data.api


import com.pinslog.ww.BuildConfig
import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

interface WeatherApi {

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

    // ===== flow =====
    @GET("weather")
    suspend fun getCurrentWeather(@Query("lat") lat: Double,
                                  @Query("lon") lon: Double,
                                  @Query("appid") appid: String = BuildConfig.APP_KEY,
                                  @Query("lang") lang: String = LANG): Response<WeatherLatLng>

    @GET("forecast")
    suspend fun getForecast(@Query("lat") lat: Double,
                            @Query("lon") lon: Double,
                            @Query("appid") appid: String = BuildConfig.APP_KEY,
                            @Query("lang") lang: String = LANG): Response<WeatherResponse>

    companion object {
        const val LANG = "kr"
    }


}