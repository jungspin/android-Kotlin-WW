package com.pinslog.ww.presentation.viewmodel

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.pinslog.ww.BuildConfig
import com.pinslog.ww.R
import com.pinslog.ww.domain.usecase.WeatherUseCase
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.presentation.model.CurrentWeather
import com.pinslog.ww.presentation.model.HourlyForecast
import com.pinslog.ww.presentation.model.Status
import com.pinslog.ww.presentation.model.UiState
import com.pinslog.ww.presentation.view.screens.LOG_TAG
import com.pinslog.ww.util.CURRENT_TIME_PATTERN
import com.pinslog.ww.util.Utility
import com.pinslog.ww.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private var currentMutableData = SingleLiveEvent<UiState<CurrentWeather>>()
    private var forecastMutableData = SingleLiveEvent<MutableList<ForecastDO?>?>()

    //private val weatherRepository = WeatherRepository()
    private lateinit var disposable: Disposable

    // StateFlow
    private var _currentWeatherStateFlow = MutableStateFlow<UiState<CurrentWeather>>(UiState())
    val currentWeatherStateFlow = _currentWeatherStateFlow.asStateFlow()

    private var _forecastWeatherStateFlow =
        MutableStateFlow<UiState<MutableList<ForecastDO?>>>(UiState())
    val forecastWeather = _forecastWeatherStateFlow.asStateFlow()

    init {
        _currentWeatherStateFlow.value = UiState(
            status = Status.LOADING,
            data = CurrentWeather(
                currentLocation = "위치를 불러오는 중입니다..",
                weatherIcon = R.raw.lottie_loading_ww
            )
        )
    }

    val getValue: MutableLiveData<UiState<CurrentWeather>>
        get() = currentMutableData

    val getForecastValue: MutableLiveData<MutableList<ForecastDO?>?>
        get() = forecastMutableData

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        // 개발 중 테스트 좌표
        if (BuildConfig.BUILD_TYPE == "debug") {
            loadWeatherData(35.173734, 129.128574)
            return
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // TODO: 위치 비활성화 상태 처리
        } else {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location == null) {
                        val locationCallback = object : LocationCallback() {
                            override fun onLocationResult(result: LocationResult) {
                                super.onLocationResult(result)
                                result.locations.forEach { location ->
                                    loadWeatherData(location.latitude, location.longitude)
                                }
                            }
                        }
                        fusedLocationProviderClient.requestLocationUpdates(
                            requestRequest, locationCallback, Looper.myLooper()
                        )
                    } else {
                        if (BuildConfig.BUILD_TYPE == "debug") {
                            Log.d(TAG, "======== FUSED_LOCATION_CLIENT lastLocation =======")
                            Log.d(TAG, "LatLng: ${location.latitude}, ${location.longitude}")
                            Log.d(TAG, "bearing: ${location.bearing}")
                            Log.d(TAG, "accuracy: ${location.accuracy}")
                            Log.d(TAG, "speed: ${location.speed}")
                            Log.d(TAG, "=============================")
                        }

                        loadWeatherData(location.latitude, location.longitude)
                    }
                }
                .addOnFailureListener {
                    // TODO: 예외처리해두면 좋겠지
                    Log.d(LOG_TAG, "getCurrentLocation: ${it.localizedMessage}")
                }
        }
    }

    /**
     * 현재 좌표를 통해 날씨 정보를 불러옵니다.
     *
     * @param currentLocationLat latitude
     * @param currentLocationLon longitude
     */
    private fun loadWeatherData(currentLocationLat: Double, currentLocationLon: Double) {
        val currentLocation = LatLng(currentLocationLat, currentLocationLon)
        getCurrentWeather(currentLocation.latitude, currentLocation.longitude)
        getForecastWeather(currentLocation.latitude, currentLocation.longitude)
    }

    private val requestRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
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

            viewModelScope.launch(Dispatchers.IO) {
                val currentAddress = getCurrentAddress(lat, lng, geocoder)
                withContext(Dispatchers.Main) {
                    currentMutableData.value = UiState(
                        status = Status.SUCCESS,
                        data = CurrentWeather(
                            currentAddress,
                            Utility.getRealTempAsString(currentTemp),
                            currentTime,
                            wearInfo,
                            weatherIcon,
                            weatherDescription
                        ),
                    )
                }
            }
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

    // ===== flow =====
    private fun getCurrentWeather(lat: Double, lon: Double) {
        _currentWeatherStateFlow.value.apply {
            viewModelScope.launch {
                weatherUseCase.getCurrentWeather(lat, lon)
                    // TODO: error 발생 시점 처리
                    .stateIn(viewModelScope)
                    .collect { response ->
                        if (response.isSuccessful) {
                            response.body()?.let { data ->
                                val currentTemp = data.main.temp
                                val currentTime =
                                    System.currentTimeMillis().toDate(CURRENT_TIME_PATTERN)
                                // 옷 정보 설정
                                val wearInfo = Utility.getWearingInfo(
                                    Utility.getRealTemp(currentTemp).toDouble()
                                )

                                val weather = data.weather[0]
                                val weatherIcon = Utility.setCodeToImg(weather.id)
                                val weatherDescription = weather.description

                                val currentAddress = getCurrentAddress(lat, lon, geocoder)
                                _currentWeatherStateFlow.value = UiState(
                                    status = Status.SUCCESS,
                                    data = CurrentWeather(
                                        currentAddress,
                                        Utility.getRealTempAsString(currentTemp),
                                        currentTime,
                                        wearInfo,
                                        weatherIcon,
                                        weatherDescription
                                    ),
                                )
                            }
                        } else {
                            _currentWeatherStateFlow.value = UiState(
                                status = Status.FAIL,
                                message = "현재 날씨를 불러오는 데에 실패했습니다.",
                            )
                        }

                    }
            }
        }

    }

    private fun getForecastWeather(lat: Double, lon: Double) {
        _forecastWeatherStateFlow.value.apply {
            viewModelScope.launch {
                weatherUseCase.getForecastWeather(lat, lon)
                    .stateIn(viewModelScope)
                    .collect { response ->
                        if (response.isSuccessful) {
                            response.body()?.let { data ->
                                val weatherList = mutableListOf<ForecastDO?>()
                                // 9 = (23, 123432)
                                val hourlyMap = mutableMapOf<String, HourlyForecast>()
                                data.list.filter {
                                    isBeforeForecast(it.dt_txt)
                                }

                                data.list.forEach {
                                    val dt = it.dt_txt.split(" ")
                                    val dateArray = dt[0].split("-")
                                    val month = dateArray[1]
                                    val date = dateArray[2]

                                    it.date = "$month-$date"
                                }

                                // 날짜별 묶음
                                val tmp = data.list.groupBy { it.date }
                                var id = 0
                                tmp.forEach { map ->
                                    // month, date
                                    val dateParts = map.key.split("-")
                                    val month = dateParts[0]
                                    val date = dateParts[1]

                                    var pop = 0.0
                                    val forecastTimeList =
                                        map.value.filter { !isBeforeForecast(it.dt_txt) }
                                            .toMutableList()
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
                                    val hourlyTemp: List<Int> =
                                        sortedMap.values.map { it.temp }

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
                                _forecastWeatherStateFlow.value = UiState(
                                    status = Status.SUCCESS,
                                    data = weatherList
                                )
                            }
                        } else {
                            _forecastWeatherStateFlow.value = UiState(
                                status = Status.FAIL,
                                message = "날씨 예보를 불러오는 데에 실패했습니다.",
                            )
                        }
                    }
            }
        }
    }

    /**
     * 이전 예보 판단
     */
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
    suspend fun getCurrentAddress(
        lat: Double,
        lng: Double,
        geocoder: Geocoder
    ): String {
        val list = geocoder.reverse(lat, lng, 10)
        val first = list.firstOrNull() ?: return ""
        return first.toKoreanShort()
    }

    @Suppress("DEPRECATION")
    suspend fun Geocoder.reverse(
        lat: Double,
        lng: Double,
        maxResults: Int = 5
    ): List<Address> = kotlinx.coroutines.suspendCancellableCoroutine { cont ->
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            getFromLocation(lat, lng, maxResults, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (cont.isActive) cont.resume(addresses) {}
                }
                override fun onError(errorMessage: String?) {
                    if (cont.isActive) cont.resume(emptyList()) {}
                }
            })
        } else {
            val res = runCatching { getFromLocation(lat, lng, maxResults) ?: emptyList() }
                .getOrDefault(emptyList())
            cont.resume(res) {}
        }
    }

    private fun Address.toKoreanShort(): String {
        // 보통: locality(구/시) ↔ subAdminArea 대체, subLocality(동/읍/면), thoroughfare(도로명)
        val gu = locality ?: subAdminArea
        val dong = subLocality ?: thoroughfare ?: featureName
        return listOfNotNull(gu, dong).joinToString(" ")
    }
}

