package com.cos.weartogo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cos.weartogo.config.RetrofitInstance
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import com.cos.weartogo.service.WeatherService

private const val TAG = "MainViewModel"

class CurrentViewModel() : ViewModel() {

    // MutableData
    private var weatherLatLng = MutableLiveData<WeatherLatLng?>()

    init {
        weatherLatLng.value = null
    }

    val getValue: MutableLiveData<WeatherLatLng?>
        get() = weatherLatLng

//    // 좌표로 날씨 가져오기
//    fun getWeatherLatLng(lat: Double, lng: Double, appid: String, lang : String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(WeatherApiConfig.BASE)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val weatherAPIService = retrofit.create(WeatherAPI::class.java)
//        weatherAPIService
//            .getWeatherLatLng(lat, lng, appid, lang)
//            .enqueue(object : Callback<WeatherLatLng> {
//                override fun onResponse(
//                    call: Call<WeatherLatLng>,
//                    response: Response<WeatherLatLng>
//                ) {
//                    weatherLatLng.value = response.body()
//                }
//                override fun onFailure(call: Call<WeatherLatLng>, t: Throwable) {
//                    Log.d(TAG, "onFailure: 실패")
//                }
//            })
//    }

    /**
     * 좌표를 통해 날씨 정보를 받아옵니다.
     */
    fun getCurrentWeatherLatLng(lat: Double, lng: Double) {
        val weatherService = WeatherService(RetrofitInstance)
        val disposable = weatherService.getCurrentWeatherLatLng(lat, lng).subscribe({
            weatherLatLng.value = it
        }, {
            it.printStackTrace()
        })
    }


}