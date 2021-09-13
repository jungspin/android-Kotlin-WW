package com.cos.weartogo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cos.weartogo.data.Item
import com.cos.weartogo.data.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private const val TAG = "MainActivity2"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeather()
    }

    private fun getWeather(){
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherAPI.DOMAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPIService = retrofit.create(WeatherAPIService::class.java)

        weatherAPIService
            .getWeather()
            .enqueue(object : Callback<Weather>{
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    //Log.d(TAG, "onResponse body: ${response.body()?.response?.body?.items?.item?.get(0)?.taMax10}")
                    var item = response.body()?.response?.body?.items?.item?.get(0)
                    if (item != null) {
                        Log.d(TAG, "onResponse: $item")
                        var cal: Calendar = Calendar.getInstance()
                        var day: Int = (cal.get(Calendar.DAY_OF_MONTH)+3)

                        Log.d(TAG, "getInstance: $day")
                        Log.d(TAG, "item: ${item.taMax3}")
                        Log.d(TAG, "item: ${item.taMin3}")


                    }
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

    }
}