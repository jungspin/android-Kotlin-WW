package com.cos.weartogo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.cos.weartogo.weatherdata.Main
import com.cos.weartogo.weatherdata.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

private const val TAG = "MainActivity2"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeatherAPI()
    }

//    private fun getWeather(){
//        val retrofit = Retrofit.Builder()
//            .baseUrl(WeatherAPI.DOMAIN)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val weatherAPIService = retrofit.create(WeatherAPIService::class.java)
//
//        weatherAPIService
//            .getWeather(/*WeatherAPI.API_KEY*/)
//            .enqueue(object : Callback<Weather>{
//                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
//                    Log.d(TAG, "onResponse body: ${response.body()?.response?.body?.items?.item?.get(0)}")
//                    var item = response.body()?.response?.body?.items?.item?.get(0)
//                    if (item != null) {
//                        var weatherDTOList = mutableListOf<WeatherDTO>(WeatherDTO(item.taMin3, item.taMax3))
//                        weatherDTOList.add(WeatherDTO(item.taMin4, item.taMax4))
//                        weatherDTOList.add(WeatherDTO(item.taMin5, item.taMax5))
//                        weatherDTOList.add(WeatherDTO(item.taMin6, item.taMax6))
//                        weatherDTOList.add(WeatherDTO(item.taMin7, item.taMax7))
//                        weatherDTOList.add(WeatherDTO(item.taMin8, item.taMax8))
//                        weatherDTOList.add(WeatherDTO(item.taMin9, item.taMax9))
//                        weatherDTOList.add(WeatherDTO(item.taMin10, item.taMax10))
//
//                        var cal: Calendar = Calendar.getInstance()
//                        var month = (cal.get(Calendar.MONTH)+1)
//
//                        for (i in 0 until weatherDTOList.size) {
//                            var day: Int = (cal.get(Calendar.DAY_OF_MONTH)+(i+3))
//                            Log.d(TAG, "${month}월 ${day}일의 최저, 최고 기온: ${weatherDTOList[i].taMin}, ${weatherDTOList[i].taMax}")
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<Weather>, t: Throwable) {
//                    t.printStackTrace()
//                    Toast.makeText(this@MainActivity, "데이터를 받아오는데 실패했습니다", Toast.LENGTH_SHORT).show()
//
//                }
//
//            })
//
//    }

    private fun getWeatherAPI(){
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherAPI.BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPIService = retrofit.create(WeatherAPIService::class.java)

        weatherAPIService
            .getWeatherAPI()
            .enqueue(object : Callback<WeatherData>{
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    Log.d(TAG, "onResponse: ${response.body()?.main?.feels_like}")
                    var c : Double? = (response.body()?.main?.feels_like)?.minus(273.15)


                    Log.d(TAG, "onResponse: ${c?.let { floor(it) }}")

                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    t.printStackTrace()
                }

            })

    }
}