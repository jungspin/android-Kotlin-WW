package com.pinslog.ww.data.data_source.impl

import com.pinslog.ww.BuildConfig
import com.pinslog.ww.data.api.WeatherApi
import com.pinslog.ww.data.data_source.WeatherRemoteDataSource
import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

private const val LANG = "kr"

class WeatherRemoteDataSourceImpl @Inject constructor(private val weatherApi: WeatherApi) :
    WeatherRemoteDataSource {
    override fun getCurrentWeatherLatLng(lat: Double, lon: Double): Single<WeatherLatLng> {
        return weatherApi.getCurrentWeatherLatLng(lat, lon, BuildConfig.APP_KEY, LANG)
    }

    override fun getForecastLatLng(lat: Double, lon: Double): Single<WeatherResponse> {
        return weatherApi.getForecastLatLng(lat, lon, BuildConfig.APP_KEY, LANG)
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Flow<Response<WeatherLatLng>> {
        return flow { emit(weatherApi.getCurrentWeather(lat, lon)) }
    }

    override suspend fun getForecast(lat: Double, lon: Double): Flow<Response<WeatherResponse>> {
        return flow { emit(weatherApi.getForecast(lat, lon)) }
    }
}