package com.cos.weartogo

import com.cos.weartogo.data.Item
import com.cos.weartogo.data.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface WeatherAPIService {

//    @GET("{api_key}&pageNo=1&numOfRows=10&dataType=json&regId=11B10101&tmFc=202109131800")
//    fun getWeather(@Path("api_key") key: String): Call<Item>

    @GET("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?serviceKey=rKyzRRphHPRl6e3E9COq4s88P1oHyXxniotNKn%2BmOvZryqw0m3ElKZSzP3HQwBXDB7eBtylBImIlOzRsv16X4Q%3D%3D&pageNo=1&numOfRows=10&dataType=json&regId=11B10101&tmFc=202109131800")
    fun getWeather(): Call<Weather>




}