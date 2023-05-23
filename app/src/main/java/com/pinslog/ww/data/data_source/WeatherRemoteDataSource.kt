package com.pinslog.ww.data.data_source

import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import io.reactivex.Single

interface WeatherRemoteDataSource {

    fun getCurrentWeatherLatLng(lat: Double, lon: Double): Single<WeatherLatLng>
    fun getForecastLatLng(lat: Double, lon: Double) : Single<WeatherResponse>
}