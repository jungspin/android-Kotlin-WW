package com.pinslog.ww.data.data_source.impl

import com.pinslog.ww.BuildConfig
import com.pinslog.ww.data.api.WeatherApi
import com.pinslog.ww.data.data_source.WeatherRemoteDataSource
import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import javax.inject.Inject

private const val LANG = "kr"
class WeatherRemoteDataSourceImpl @Inject constructor(private val weatherApi: WeatherApi) : WeatherRemoteDataSource {
    override fun getCurrentWeatherLatLng(lat: Double, lon: Double): Single<WeatherLatLng> {
        return weatherApi.getCurrentWeatherLatLng(lat, lon, BuildConfig.APP_KEY, LANG)
    }

    override fun getForecastLatLng(lat: Double, lon: Double): Single<WeatherResponse> {
        return weatherApi.getForecastLatLng(lat, lon, BuildConfig.APP_KEY, LANG)
    }
}