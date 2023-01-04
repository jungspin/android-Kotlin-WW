package com.pinslog.ww.config

import com.pinslog.ww.BuildConfig
import com.google.gson.Gson
import com.pinslog.ww.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * retrofit instance
 *
 * @author jungspin
 * @since 2022-07-10
 */
@Module
@InstallIn(SingletonComponent::class)
class RetrofitInstance {

    @Singleton
    @Provides
    fun getInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun getOkHttpClient():OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(5, 50, TimeUnit.SECONDS))
            .build()
    }

    @Singleton
    @Provides
    fun getWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }
}

