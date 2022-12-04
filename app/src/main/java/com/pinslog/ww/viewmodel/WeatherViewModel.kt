package com.pinslog.ww.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinslog.ww.config.RetrofitInstance
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.data.weatherLatLng.WeatherLatLng
import com.pinslog.ww.service.WeatherService
import com.pinslog.ww.util.Utility
import io.reactivex.disposables.Disposable

private const val TAG = "WeatherViewModel"

// TODO Repository Pattern 적용하기
class WeatherViewModel() : ViewModel() {

    // MutableData
    private var currentMutableData = MutableLiveData<WeatherLatLng?>()
    private var forecastMutableData = MutableLiveData<MutableList<ForecastDO?>?>()

    private val weatherService = WeatherService(RetrofitInstance)
    private lateinit var disposable: Disposable

    init {
        currentMutableData.value = null
        forecastMutableData.value = null
    }

    val getValue: MutableLiveData<WeatherLatLng?>
        get() = currentMutableData

    val getForecastValue: MutableLiveData<MutableList<ForecastDO?>?>
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

    /**
     * 좌표를 통해 날씨 예보를 받아옵니다.
     */
    fun getForecastLatLng(lat: Double, lng: Double) {

        disposable = weatherService.getForecastLatLng(lat, lng)
            .subscribe({ it ->

                val weatherList = mutableListOf<ForecastDO?>()
                it.list.forEach {
                    val dt = it.dt_txt.split(" ")
                    val dateArray = dt[0].split("-")
                    val month = dateArray[1]
                    val date = dateArray[2]

                    it.date = "$month-$date"
                }
                val tmp = it.list.groupBy { it.date }
                val tempList = mutableListOf<Double>()
                var id = 0
                tmp.forEach { map ->
                    // month, date
                    val dateParts = map.key.split("-")
                    val month = dateParts[0]
                    val date = dateParts[1]

                    // weather icon
                    map.value.forEach {
                        tempList.add(it.main.temp)
                        it.weather.forEach { id = it.id }
                    }
                    // min, max temp
                    val min = tempList.minOf { Utility.getRealTemp(it) }
                    val max = tempList.maxOf { Utility.getRealTemp(it) }

                    val forecastDO = ForecastDO(
                        month = month,
                        date = date,
                        id = id,
                        maxTemp = max,
                        minTemp = min,
                    )
                    weatherList.add(forecastDO)
                }
                forecastMutableData.value = weatherList
            }) { it.printStackTrace() }
    }


}