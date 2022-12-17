package com.pinslog.ww.viewmodel

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinslog.ww.config.RetrofitInstance
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.data.weatherLatLng.WeatherLatLng
import com.pinslog.ww.service.WeatherRepository
import com.pinslog.ww.util.Utility
import io.reactivex.disposables.Disposable
import java.time.LocalDateTime

private const val TAG = "WeatherViewModel"

// TODO Repository Pattern 적용하기
class WeatherViewModel() : ViewModel() {

    // MutableData
    private var currentMutableData = MutableLiveData<WeatherLatLng?>()
    private var forecastMutableData = MutableLiveData<MutableList<ForecastDO?>?>()

    private val weatherRepository = WeatherRepository()
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
        disposable = weatherRepository.getCurrentWeatherLatLng(lat, lng).subscribe({
            currentMutableData.value = it
        }, {
            it.printStackTrace()
        })
    }

    /**
     * 좌표를 통해 날씨 예보를 받아옵니다.
     */
    fun getForecastLatLng(lat: Double, lng: Double) {
        disposable = weatherRepository.getForecastLatLng(lat, lng)
            .subscribe({ it ->

                val weatherList = mutableListOf<ForecastDO?>()
                it.list.filter {
                    isBeforeForecast(it.dt_txt)
                }
                it.list.forEach {
                    val dt = it.dt_txt.split(" ")
                    val dateArray = dt[0].split("-")
                    val month = dateArray[1]
                    val date = dateArray[2]

                    it.date = "$month-$date"
                }

                val tmp = it.list.groupBy { it.date }

                var id = 0
                tmp.forEach { map ->
                    // month, date
                    val dateParts = map.key.split("-")
                    val month = dateParts[0]
                    val date = dateParts[1]

                    // weather icon
                    val maxTempList = mutableListOf<Double>()
                    val minTempList = mutableListOf<Double>()
                    map.value.forEach {

                        maxTempList.add(it.main.temp_max)
                        minTempList.add(it.main.temp_min)
                        it.weather.forEach { id = it.id }
                    }
                    // min, max temp
                    val minValue = minTempList.minOf { it }
                    val maxValue = maxTempList.maxOf { it }

                    val forecastDO = ForecastDO(
                        month = month,
                        date = date,
                        id = id,
                        maxTemp = Utility.getRealTemp(maxValue),
                        minTemp = Utility.getRealTemp(minValue),
                    )
                    maxTempList.clear()
                    minTempList.clear()
                    weatherList.add(forecastDO)
                }
                forecastMutableData.value = weatherList
            }) { it.printStackTrace() }
    }


    // 이게..의미가 있나..? 일단은 그냥 두는게..나을지도
    private fun isBeforeForecast(dateText: String): Boolean{
        val dateTextParts = dateText.split(" ")
        val datePart = dateTextParts[0].split("-")
        val timePart = dateTextParts[1].split(":")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val forecastDate = LocalDateTime.of(
                datePart[0].toInt(),
                datePart[1].toInt(),
                datePart[2].toInt(),
                timePart[0].toInt(),
                0
            )
            val currentDate = LocalDateTime.now()
            return forecastDate.isBefore(currentDate)
        } else {
            // TODO 구현
            return false
        }

    }


}