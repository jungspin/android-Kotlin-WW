package com.pinslog.ww.data.data_source

import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRemoteDataSource {

    fun getCurrentWeatherLatLng(lat: Double, lon: Double): Single<WeatherLatLng>
    fun getForecastLatLng(lat: Double, lon: Double) : Single<WeatherResponse>

    // ===== flow =====
    suspend fun getCurrentWeather(lat: Double, lon: Double): Flow<Response<WeatherLatLng>>
    suspend fun getForecast(lat: Double, lon: Double): Flow<Response<WeatherResponse>>
}