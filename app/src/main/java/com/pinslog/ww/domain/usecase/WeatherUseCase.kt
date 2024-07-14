package com.pinslog.ww.domain.usecase

import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import com.pinslog.ww.domain.repository.WeatherRepository
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    fun getCurrentWeatherLatLng(lat: Double, lon: Double): Single<WeatherLatLng> =
        weatherRepository.getCurrentWeatherLatLng(lat, lon)

    fun getForecastLatLng(lat: Double, lon: Double): Single<WeatherResponse> =
        weatherRepository.getForecastLatLng(lat, lon)

    // ===== flow =====
    suspend fun getCurrentWeather(lat: Double, lon: Double): Flow<Response<WeatherLatLng>> =
        weatherRepository.getCurrentWeather(lat, lon)

    suspend fun getForecastWeather(lat: Double, lon: Double): Flow<Response<WeatherResponse>> =
        weatherRepository.getForecastWeather(lat, lon)

}