package com.cos.weartogo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cos.weartogo.data.weatherCity.WeatherData
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import com.cos.weartogo.databinding.ActivityMainBinding

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import kotlin.math.floor

private const val TAG = "MainActivity2"

class MainActivity : AppCompatActivity() {

    private var mContext: Context = this

    private lateinit var binding: ActivityMainBinding

    private var locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 도시로 날씨 받아오기
//        var city = "seoul"
//        getWeatherAPI(city)


        // 좌표로 날씨 받아오기
        var location: Location? = getMyLocation()
        if (location != null) {
            var lat = location?.latitude
            var lng = location?.longitude
            Log.d(TAG, "gps: $lat, $lng")
            if (lat != null && lng != null) {
                Log.d(TAG, "onCreate: $lat, $lng")
            }
            getWeatherLatLng(lat, lng, WeatherAPI.KEY)

        } else {
            Log.d(TAG, "onCreate: null")
        }


        getMyLocation()

        initLr()

    }

    private fun initLr() {
        binding.btnDetail.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "initLr: 상세보기 클릭")
        })
        binding.btnCityChange.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "initLr: 도시 변경 클릭")
        })
    }


    // 도시 이름으로 날씨 받아오기 ==============================================
    private fun getWeatherAPI(city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherAPI.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPIService = retrofit.create(WeatherAPIService::class.java)

        weatherAPIService
            .getWeatherAPI(city, WeatherAPI.KEY)
            .enqueue(object : Callback<WeatherData> {
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    Log.d(TAG, "onResponse: $response")

                    val sdf = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분")
                    Log.d(TAG, "currentTime: ${sdf.format(System.currentTimeMillis())} ")

                    var main = response.body()?.main
                    if (main != null) {

                        binding.tvCity.text = city
                        binding.tvMax.text = "현재 온도 : ${getRealTemp(main.temp)}"
                        binding.tvMin.text = "체감 온도 : ${getRealTemp(main.feels_like)}"
                    }

                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    t.printStackTrace()
                }

            })

    }

    // 좌표로 날씨 받아오기 =========================================================
    private fun getWeatherLatLng(lat: Double, lng: Double, appid: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherAPI.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPIService = retrofit.create(WeatherAPIService::class.java)
        weatherAPIService
            .getWeatherLngLat(lat, lng, appid)
            .enqueue(object : Callback<WeatherLatLng> {
                override fun onResponse(
                    call: Call<WeatherLatLng>,
                    response: Response<WeatherLatLng>
                ) {
                    Log.d(TAG, "onResponse: 성공 ")
                    val sdf = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분")
                    Log.d(TAG, "currentTime: ${sdf.format(System.currentTimeMillis())} ")

                    Log.d(TAG, "onResponse: ${response.body()?.main?.feels_like}")
                    var main = response.body()?.main
                    if (main != null) {

                        binding.tvCity.text = "${sdf.format(System.currentTimeMillis())}"
                        binding.tvMax.text = "현재 온도 : ${getRealTemp(main.temp)}"
                        binding.tvMin.text = "체감 온도 : ${getRealTemp(main.feels_like)}"
                    }
                }

                override fun onFailure(call: Call<WeatherLatLng>, t: Throwable) {
                    Log.d(TAG, "onFailure: 실패")
                }

            })
    }

    // 좌표 구하기 =======================================================
    private fun getMyLocation(): Location? {
        Log.d(TAG, "getMyLocation: 함수 때려짐")
        var currentLocation: Location? = null
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "getMyLocation: 권한 부여 되지 않음")
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            //getMyLocation() //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        } else {
            Log.d(TAG, "getMyLocation: 권한 부여됨")

            // 수동으로 위치 구하기
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val locationProvider = LocationManager.NETWORK_PROVIDER
            currentLocation = locationManager!!.getLastKnownLocation(locationProvider)
            if (currentLocation != null) {
                val lng = currentLocation.longitude
                val lat = currentLocation.latitude
                Log.d(TAG, "getMyLocation: $lat, $lng")
            }
        }
        return currentLocation
    }




    private fun getRealTemp(temp: Double): String {
        var c = temp - 273.15
        return floor(c).toString()
    }


}