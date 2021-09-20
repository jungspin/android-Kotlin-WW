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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cos.weartogo.data.weatherCity.WeatherData
import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import com.cos.weartogo.databinding.ActivityMainBinding
import com.cos.weartogo.viewmodel.MainViewModel

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

    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 도시로 날씨 받아오기
//        var city = "seoul"
//        getWeatherAPI(city)


        initLr()
        initSetting()
        initData()



    }

    private fun initLr() {

    }

    private fun initSetting() {
        // 뷰모델 프로바이더를 통해 뷰모델 가져오기
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]


    }

    private fun initData() {
        // 좌표로 날씨 받아오기
        var location: Location? = getMyLocation()
        if (location != null) {
            var lat = location?.latitude
            var lng = location?.longitude
            Log.d(TAG, "gps: $lat, $lng")
            if (lat != null && lng != null) {
                Log.d(TAG, "onCreate: $lat, $lng")
            }
            mainViewModel.getWeatherLatLng(lat, lng, WeatherAPI.KEY, WeatherAPI.LANG)
            // 뷰모델이 가지고 있는 값의 변경사항을 관찰할 수 있는 라이브 데이터를 관찰한다
            mainViewModel.getValue.observe(this, Observer {
                if(it != null) {

                    var temp = getRealTemp(it?.main?.temp)
                    binding.tvTemp.text = "$temp °C"
                    getInfo(temp)
                    setCodeToImg(it?.weather?.get(0)?.id)
                    binding.tvDescription.text = it?.weather?.get(0)?.description

                    val sdf = SimpleDateFormat("hh시 mm분")
                    binding.tvTime.text = "${sdf.format(System.currentTimeMillis())}"

                    binding.tvDescription.text = "${it.weather?.get(0)?.description}"

                    binding.tvLocation.text = "${it?.name}"




                }

            })

        } else {
            Log.d(TAG, "onCreate: null")
        }


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




    private fun getRealTemp(temp: Double): Double {
        var c = temp - 273.15
        return floor(c)
    }

    private fun setCodeToImg(code : Int){
        when (code){
            in 200..232 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_thunder)
            }
            in 300..321 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_small_rainy)
            }
            in 500..531->{
                binding.ivWeather.setImageResource(R.drawable.ic_rainy_2)
            }
            in 600..522->{
                binding.ivWeather.setImageResource(R.drawable.ic_snowy)
            }
            in 700..781->{
                binding.ivWeather.setImageResource(R.drawable.ic_dusty)
            }
            800 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_clear)
            }
            801 ->{
                binding.ivWeather.setImageResource(R.drawable.ic_small_cloudy)
            }
            802, 803->{
                binding.ivWeather.setImageResource(R.drawable.ic_cloudy_2)
            }
            804 -> {
                binding.ivWeather.setImageResource(R.drawable.ic_more_cloudy)
            }
        }
    }

    private fun getInfo(temp: Double){
        if (temp >= 28.0){
            binding.tvInfo1.text = getString(R.string.description_28)
        }
        when (temp) {
            in 23.0..27.0 ->{
                binding.tvInfo1.text = getString(R.string.description_23)
            }
            in 20.0..22.0 ->{
                binding.tvInfo1.text = getString(R.string.description_20)
            }
            in 17.0..19.0 -> {
                binding.tvInfo1.text = getString(R.string.description_17)
            }
            in 12.0..16.0 ->{
                binding.tvInfo1.text = getString(R.string.description_12)
            }
            in 9.0..11.0 ->{
                binding.tvInfo1.text = getString(R.string.description_9)
            }
            in 5.0..8.0 ->{
                binding.tvInfo1.text = getString(R.string.description_5)
            }
        }
        if (temp <= 4.0){
            binding.tvInfo1.text = getString(R.string.description_4)
        }
    }


}