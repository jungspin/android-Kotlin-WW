package com.cos.weartogo.service


import com.cos.weartogo.data.weatherLatLng.WeatherLatLng
import com.cos.weartogo.data.weatherCity.WeatherData
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Query

interface WeatherAPIService {


//    @GET("{key}&pageNo=1&numOfRows=10&dataType=json&regId=11H20201&tmFc=202109131800")
//    fun getWeather(@Path("key") key: String): Call<Weather>

//    @GET("getMidTa?serviceKey=rKyzRRphHPRl6e3E9COq4s88P1oHyXxniotNKn%2BmOvZryqw0m3ElKZSzP3HQwBXDB7eBtylBImIlOzRsv16X4Q%3D%3D&pageNo=1&numOfRows=10&dataType=json&regId=11H20201&tmFc=202109131800")
//    fun getWeather(): Call<Weather>

    @GET("weather")
    fun getWeatherAPI(@Query("q") q: String, @Query("appid") appid: String): Call<WeatherData>

    @GET("weather")
    fun getWeatherLatLng(@Query("lat") lat: Double,
                         @Query("lon") lon: Double,
                         @Query("appid") appid: String,
                         @Query("lang")lang: String) : Call<WeatherLatLng>




}