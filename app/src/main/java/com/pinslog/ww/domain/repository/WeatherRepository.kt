package com.pinslog.ww.domain.repository

import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/**
* WeatherRepository
* @author jungspin
* @since 2023/05/23 10:38 PM
*/
interface WeatherRepository {
    fun getCurrentWeatherLatLng(
        lat: Double,
        lon: Double,
    ): Single<WeatherLatLng> 

    fun getForecastLatLng(
        lat: Double,
        lon: Double,
    ): Single<WeatherResponse>

    // ===== flow =====
    suspend fun getCurrentWeather(lat: Double, lon: Double): Flow<Response<WeatherLatLng>>
    suspend fun getForecastWeather(lat: Double, lon: Double): Flow<Response<WeatherResponse>>
}

