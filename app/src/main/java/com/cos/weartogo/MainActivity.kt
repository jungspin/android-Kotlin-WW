package com.cos.weartogo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.cos.weartogo.config.LocationHelper
import com.cos.weartogo.databinding.ActivityMainBinding

import com.cos.weartogo.weatherdata.WeatherData
import com.google.android.gms.maps.model.LatLng
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

private const val TAG = "MainActivity2"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val REQUEST_CODE_LOCATION = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var city = "seoul"

        //getWeatherAPI(city)
        initLr()






    }

    private fun initLr(){
        binding.btnDetail.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "initLr: 상세보기 클릭")
        })
        binding.btnCityChange.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "initLr: 도시 변경 클릭")
        })
    }


    private fun getWeatherAPI(city: String){
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherAPI.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPIService = retrofit.create(WeatherAPIService::class.java)



        weatherAPIService
            .getWeatherAPI(city, WeatherAPI.KEY)
            .enqueue(object : Callback<WeatherData>{
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    //Log.d(TAG, "onResponse: ${response.body()?.main?.feels_like}")

                    val sdf = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분")
                    Log.d(TAG, "currentTime: ${sdf.format(System.currentTimeMillis())} ")

                    var main = response.body()?.main
                    if (main != null){

                        binding.tvCity.text = city
                        binding.tvMax.text = "최고 기온 : ${getRealTemp(main.temp_max)}"
                        binding.tvMin.text = "최저 기온 : ${getRealTemp(main.temp_min)}"
                    }

                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    t.printStackTrace()
                }

            })

    }




    private fun getRealTemp(temp: Double): String {
        var c = temp - 273.15
        return floor(c).toString()
    }
}