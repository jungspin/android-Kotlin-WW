package com.cos.weartogo

import com.cos.weartogo.data.Item
import com.cos.weartogo.data.Weather
import com.cos.weartogo.weatherdata.Main
import com.cos.weartogo.weatherdata.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

interface WeatherAPIService {


//    @GET("{key}&pageNo=1&numOfRows=10&dataType=json&regId=11H20201&tmFc=202109131800")
//    fun getWeather(@Path("key") key: String): Call<Weather>

    @GET("getMidTa?serviceKey=rKyzRRphHPRl6e3E9COq4s88P1oHyXxniotNKn%2BmOvZryqw0m3ElKZSzP3HQwBXDB7eBtylBImIlOzRsv16X4Q%3D%3D&pageNo=1&numOfRows=10&dataType=json&regId=11H20201&tmFc=202109131800")
    fun getWeather(): Call<Weather>

    @GET("weather?q=Busan&appid=b016ea25b2f8daca52324260f8776eea")
    fun getWeatherAPI(): Call<WeatherData>




}