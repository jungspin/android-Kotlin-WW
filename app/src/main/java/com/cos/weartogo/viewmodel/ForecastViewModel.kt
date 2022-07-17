package com.cos.weartogo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cos.weartogo.config.RetrofitInstance
import com.cos.weartogo.data.openweather.OpenWeather
import com.cos.weartogo.data.openweather.WeatherResponse
import com.cos.weartogo.service.WeatherService
import com.google.gson.Gson
import io.reactivex.functions.Consumer
import kotlin.math.ln

private const val TAG = "ForecastViewModel"
class ForecastViewModel : ViewModel() {

    private var weatherResponse = MutableLiveData<WeatherResponse?>()

    init {
        weatherResponse.value = null
    }

    val getValue: MutableLiveData<WeatherResponse?>
        get() = weatherResponse

    fun getForecastLatLng(lat: Double, lng : Double){
        val weatherService = WeatherService(RetrofitInstance)
        val disposable = weatherService.getForecastLatLng(lat, lng)
            .subscribe({

//                val gson = Gson()
//                val list: Array<OpenWeather> = gson.fromJson(it.list, Array<OpenWeather>::class.java)
//                Log.d(TAG, "getForecastLatLng: ${list.get(0)}")
                weatherResponse.value = it
                Log.d(TAG, "getForecastLatLng: ${it}")
            }) { it.printStackTrace() }
    }
}