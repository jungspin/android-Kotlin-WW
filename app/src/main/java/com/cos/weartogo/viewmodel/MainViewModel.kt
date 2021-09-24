package com.cos.weartogo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.cos.weartogo.config.WeatherAPI
import com.cos.weartogo.service.WeatherAPIService
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainViewModel"

class MainViewModel() : ViewModel() {

    // MutableData
    private var weatherLatLng = MutableLiveData<WeatherLatLng?>()

    init {
        weatherLatLng.value = null
    }

    val getValue: MutableLiveData<WeatherLatLng?>
        get() = weatherLatLng

    // 좌표로 날씨 가져오기
    fun getWeatherLatLng(lat: Double, lng: Double, appid: String, lang : String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherAPI.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPIService = retrofit.create(WeatherAPIService::class.java)
        weatherAPIService
            .getWeatherLatLng(lat, lng, appid, lang)
            .enqueue(object : Callback<WeatherLatLng> {
                override fun onResponse(
                    call: Call<WeatherLatLng>,
                    response: Response<WeatherLatLng>
                ) {
                    weatherLatLng.value = response.body()
                }
                override fun onFailure(call: Call<WeatherLatLng>, t: Throwable) {
                    Log.d(TAG, "onFailure: 실패")
                }
            })
    }


}