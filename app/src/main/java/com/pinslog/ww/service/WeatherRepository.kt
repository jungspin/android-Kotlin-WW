package com.pinslog.ww.service

import com.pinslog.ww.BuildConfig
import com.pinslog.ww.config.RetrofitInstance
import com.pinslog.ww.data.openweather.WeatherResponse
import com.pinslog.ww.data.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherRepository () {

    companion object{
        const val LANG = "kr"
    }

    private var weatherApi : WeatherAPI = RetrofitInstance.getInstance().create(WeatherAPI::class.java)

    fun getCurrentWeatherLatLng(lat: Double, lng: Double) : Single<WeatherLatLng>{
        return weatherApi.getCurrentWeatherLatLng(lat, lng, BuildConfig.APP_KEY, LANG)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun getForecastLatLng(lat: Double, lng: Double) : Single<WeatherResponse>{
        return weatherApi.getForecastLatLng(lat, lng, BuildConfig.APP_KEY, LANG)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}