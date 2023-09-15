package com.pinslog.ww.presentation.viewmodel

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.domain.usecase.WeatherUseCase
import com.pinslog.ww.presentation.model.HourlyForecast
import com.pinslog.ww.model.LatLng
import com.pinslog.ww.presentation.model.CurrentWeather
import com.pinslog.ww.util.CURRENT_TIME_PATTERN
import com.pinslog.ww.util.Utility
import com.pinslog.ww.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import java.time.LocalDateTime
import javax.inject.Inject

private const val TAG = "WeatherViewModel"


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationManager: LocationManager,
    private val geocoder: Geocoder,
) : ViewModel() {

    // MutableData
    private var currentMutableData = SingleLiveEvent<CurrentWeather?>()
    private var forecastMutableData = SingleLiveEvent<MutableList<ForecastDO?>?>()

    //private val weatherRepository = WeatherRepository()
    private lateinit var disposable: Disposable

    init {
        currentMutableData.value = null
        forecastMutableData.value = null
    }

    val getValue: MutableLiveData<CurrentWeather?>
        get() = currentMutableData

    val getForecastValue: MutableLiveData<MutableList<ForecastDO?>?>
        get() = forecastMutableData

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // TODO: 위치 비활성화 상태 처리
        } else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    val currentLocation = LatLng(location.latitude, location.longitude)

                    Log.d(TAG, "======== FUSED_LOCATION_CLIENT =======")
                    Log.d(TAG, "LatLng: ${currentLocation}")
                    Log.d(TAG, "bearing: ${location.bearing}")
                    Log.d(TAG, "accuracy: ${location.accuracy}")
                    Log.d(TAG, "speed: ${location.speed}")
                    Log.d(TAG, "=============================")

                    getCurrentWeatherLatLng(currentLocation.lat, currentLocation.lng)
                    getForecastLatLng(currentLocation.lat, currentLocation.lng)
                }

        }
    }

    /**
     * 좌표를 통해 날씨 정보를 받아옵니다.
     */
    private fun getCurrentWeatherLatLng(lat: Double, lng: Double) {
        disposable = weatherUseCase.getCurrentWeatherLatLng(lat, lng).subscribe({
            val currentTemp = it.main.temp
            val currentTime = System.currentTimeMillis().toDate(CURRENT_TIME_PATTERN)
            // 옷 정보 설정
            val wearInfo = Utility.getWearingInfo(Utility.getRealTemp(currentTemp).toDouble())

            val weather = it.weather[0]
            val weatherIcon = Utility.setCodeToImg(weather.id)
            val weatherDescription = weather.description

            val currentAddress = getCurrentAddress(lat, lng, geocoder)
            currentMutableData.value = CurrentWeather(
                currentAddress,
                Utility.getRealTempAsString(currentTemp),
                currentTime,
                wearInfo,
                weatherIcon,
                weatherDescription
            )
        }, {
            it.printStackTrace()
        })
    }

    /**
     * 좌표를 통해 날씨 예보를 받아옵니다.
     */
    private fun getForecastLatLng(lat: Double, lng: Double) {
        disposable = weatherUseCase.getForecastLatLng(lat, lng)
            .subscribe({ it ->
                val weatherList = mutableListOf<ForecastDO?>()
                // 9 = (23, 123432)
                val hourlyMap = mutableMapOf<String, HourlyForecast>()
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

                // 날짜별 묶음
                val tmp = it.list.groupBy { it.date }
                var id = 0
                tmp.forEach { map ->
                    // month, date
                    val dateParts = map.key.split("-")
                    val month = dateParts[0]
                    val date = dateParts[1]

                    var pop = 0.0
                    val forecastTimeList =
                        map.value.filter { !isBeforeForecast(it.dt_txt) }.toMutableList()
                    if (forecastTimeList.isEmpty()) forecastTimeList.add(map.value.last())
                    forecastTimeList.forEach {
                        val dt = it.dt_txt.split(" ")
                        val timeArray = dt[1].split(":")
                        val time = timeArray[0]

                        // weather icon
                        it.weather.forEach { weather ->
                            id = weather.id

                            hourlyMap[time] = HourlyForecast(
                                time = time.toInt(),
                                resourceId = Utility.setCodeToImg(weather.id),
                                temp = Utility.getRealTemp(it.main.temp)
                            )
                        }
                        pop = it.pop * 100
                    }
                    val sortedMap = hourlyMap.toSortedMap()

                    // min, max temp
                    val hourlyTemp: List<Int> = sortedMap.values.map { it.temp }

                    val forecastDO = ForecastDO(
                        month = month,
                        date = date,
                        id = id,
                        maxTemp = hourlyTemp.maxOf { it }.toString(),
                        minTemp = hourlyTemp.minOf { it }.toString(),
                        pop = pop.toInt(),
                        hourlyMap = sortedMap,
                    )
                    weatherList.add(forecastDO)
                }
                forecastMutableData.value = weatherList
            }) { it.printStackTrace() }
    }

    private fun isBeforeForecast(dateText: String): Boolean {
        val dateTextParts = dateText.split(" ")
        val datePart = dateTextParts[0].split("-")
        val timePart = dateTextParts[1].split(":")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val forecastDate = LocalDateTime.of(
                datePart[0].toInt(),
                datePart[1].toInt(),
                datePart[2].toInt(),
                timePart[0].toInt(),
                0
            )
            val currentDate = LocalDateTime.now()
            forecastDate.isBefore(currentDate)
        } else {
            false
        }
    }

    /**
     * 위도 및 경도를 주소로 변환합니다.
     * @param lat 위도
     * @param lng 경도
     * @return 변환된 주소값
     */
    private fun getCurrentAddress(lat: Double, lng: Double, geocoder: Geocoder): String {
        var addressList: List<Address> = mutableListOf()

        try {
            addressList = geocoder.getFromLocation(lat, lng, 10)
        } catch (e: Exception) {
            // TODO: 처리 필요
            e.printStackTrace()
        }

        if (addressList.isNotEmpty()) {
            val addr = addressList[0].getAddressLine(0)
            val addrParts = addr.split(" ")
            return "${addrParts[2]} ${addrParts[3]}"
        }
        return ""
    }
}

