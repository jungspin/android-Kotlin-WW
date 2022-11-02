package com.cos.weartogo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cos.weartogo.config.RetrofitInstance
import com.cos.weartogo.data.openweather.WeatherResponse
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import com.cos.weartogo.service.WeatherService
import io.reactivex.disposables.Disposable

private const val TAG = "MainViewModel"

class WeatherViewModel() : ViewModel() {

    // MutableData
    private var currentMutableData = MutableLiveData<WeatherLatLng?>()
    private var forecastMutableData = MutableLiveData<WeatherResponse?>()

    private val weatherService = WeatherService(RetrofitInstance)
    private lateinit var disposable: Disposable

    init {
        currentMutableData.value = null
        forecastMutableData.value = null
    }

    val getValue: MutableLiveData<WeatherLatLng?>
        get() = currentMutableData

    val getForecastValue: MutableLiveData<WeatherResponse?>
        get() = forecastMutableData


    /**
     * 좌표를 통해 날씨 정보를 받아옵니다.
     */
    fun getCurrentWeatherLatLng(lat: Double, lng: Double) {
        val weatherService = WeatherService(RetrofitInstance)
        disposable = weatherService.getCurrentWeatherLatLng(lat, lng).subscribe({
            currentMutableData.value = it
        }, {
            it.printStackTrace()
        })
    }

    fun getForecastLatLng(lat: Double, lng: Double) {

        disposable = weatherService.getForecastLatLng(lat, lng)
            .subscribe({

//                val gson = Gson()
//                val list: Array<OpenWeather> = gson.fromJson(it.list, Array<OpenWeather>::class.java)
//                Log.d(TAG, "getForecastLatLng: ${list.get(0)}")
                forecastMutableData.value = it
                Log.d(TAG, "getForecastLatLng: ${it}")
            }) { it.printStackTrace() }
    }


}