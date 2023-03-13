package com.pinslog.ww.service

import com.pinslog.ww.BuildConfig
import com.pinslog.ww.data.openweather.WeatherResponse
import com.pinslog.ww.data.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val LANG = "kr"

class WeatherRepository @Inject constructor(private val weatherService: WeatherService) {

    fun getCurrentWeatherLatLng(
        lat: Double,
        lon: Double,
    ): Single<WeatherLatLng> {
        return weatherService.getCurrentWeatherLatLng(lat, lon, BuildConfig.APP_KEY, LANG)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getForecastLatLng(
        lat: Double,
        lon: Double,
    ): Single<WeatherResponse> {
        return weatherService.getForecastLatLng(lat, lon, BuildConfig.APP_KEY, LANG)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}