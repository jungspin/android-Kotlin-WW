package com.cos.weartogo.service

import com.cos.weartogo.BuildConfig
import com.cos.weartogo.config.RetrofitInstance
import com.cos.weartogo.data.openweather.WeatherResponse
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherService (retrofitInstance: RetrofitInstance) {

    companion object{
        const val LANG = "kr"
    }

    private var weatherApi : WeatherAPI = retrofitInstance.getInstance().create(WeatherAPI::class.java)

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