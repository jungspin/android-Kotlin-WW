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
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
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
import kotlin.properties.Delegates

 private const val TAG = "MainActivity2"

class MainActivity : AppCompatActivity() {

    private var mContext: Context = this

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel
    private val mLocation = CustomLocation(mContext)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLr()
        initSetting()
        initData()



    }

    private fun initLr() {
        binding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                if (p0 != null) {
                    Log.d(TAG, "onQueryTextSubmit: $p0")
                    mLocation.addrToLatLng(p0)
                    var qLat = mLocation.addrToLatLng(p0)?.latitude
                    var qLng = mLocation.addrToLatLng(p0)?.longitude

                    if(qLat != null && qLng != null){
                        // 위치 이름 설정
                        var addr = mLocation.latLngToAddr(qLat, qLng)
                     
                        var address = "${addr?.adminArea} ${addr?.locality} ${addr?.thoroughfare}"
                        if (address.contains("null")){
                            address = address.replace("null", "")
                            binding.tvLocation.text = address
                        }
                        binding.tvLocation.text = address

                        mainViewModel.getWeatherLatLng(qLat, qLng, WeatherAPI.KEY, WeatherAPI.LANG)
                        // 뷰모델이 가지고 있는 값의 변경사항을 관찰할 수 있는 라이브 데이터를 관찰한다
                        mainViewModel.getValue.observe(this@MainActivity, Observer {
                            if(it != null) {

                                // 현재 온도
                                var temp = getRealTemp(it?.main?.temp)
                                binding.tvTemp.text = "$temp °C"
                                // 옷 정보 설정
                                setInfo(temp)
                                // 날씨 아이콘 설정
                                setCodeToImg(it?.weather?.get(0)?.id)
                                // 날씨 설명
                                binding.tvDescription.text = it?.weather?.get(0)?.description
                                // 현재 시간
                                val sdf = SimpleDateFormat("hh시 mm분")
                                binding.tvTime.text = "${sdf.format(System.currentTimeMillis())}"



                            }
                        })
                    }
                } else {
                    // gps == null
                    Toast.makeText(this@MainActivity, "일시적으로 위치 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Log.d(TAG, "onQueryTextChange: ")
                return false
            }

        })

    }

    private fun initSetting() {
        // 뷰모델 프로바이더를 통해 뷰모델 가져오기
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]


    }

    private fun initData() {
        // 좌표로 날씨 받아오기


        if (mLocation != null) {
            var lat = mLocation?.getMyLocation()?.latitude
            var lng = mLocation?.getMyLocation()?.longitude

            if(lat != null && lng != null){
                // 위치 이름 설정
                var addr = mLocation.latLngToAddr(lat, lng)
                var address = "${addr?.adminArea} ${addr?.locality} ${addr?.thoroughfare}"
                binding.tvLocation.text = address

                mainViewModel.getWeatherLatLng(lat, lng, WeatherAPI.KEY, WeatherAPI.LANG)
                // 뷰모델이 가지고 있는 값의 변경사항을 관찰할 수 있는 라이브 데이터를 관찰한다
                mainViewModel.getValue.observe(this, Observer {
                    if(it != null) {

                        // 현재 온도
                        var temp = getRealTemp(it?.main?.feels_like)
                        binding.tvTemp.text = "$temp °C"
                        // 옷 정보 설정
                        setInfo(temp)
                        // 날씨 아이콘 설정
                        setCodeToImg(it?.weather?.get(0)?.id)
                        // 날씨 설명
                        binding.tvDescription.text = it?.weather?.get(0)?.description
                        // 현재 시간
                        val sdf = SimpleDateFormat("hh시 mm분")
                        binding.tvTime.text = "${sdf.format(System.currentTimeMillis())}"


                    }
                })
            }
        } else {
            // gps == null
            Toast.makeText(this, "일시적으로 위치 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
        }
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

    private fun setInfo(temp: Double){
        if (temp >= 28.0){
            binding.tvInfo1.text = getString(R.string.description_28)
        }
        when (temp) {
            in 23.0..27.0 ->{
                binding.tvInfo1.text = getString(R.string.description_23)
                binding.ivInfo1.setImageResource(R.drawable.ic_long_sleeve)
                binding.ivInfo2.setImageResource(R.drawable.ic_cotton_pants)
                binding.ivInfo3.setImageResource(R.drawable.ic_light_cardigan)
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