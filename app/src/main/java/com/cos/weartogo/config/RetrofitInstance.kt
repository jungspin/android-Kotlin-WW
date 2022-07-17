package com.cos.weartogo.config

import com.cos.weartogo.BuildConfig
import com.google.gson.Gson
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * retrofit instance
 *
 * @author jungspin
 * @since 2022-07-10
 */
object RetrofitInstance {
    private var retrofit: Retrofit = Retrofit.Builder().client(
        OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(5, 50, TimeUnit.SECONDS))
            .build()
    )

        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .build()


    fun getInstance(): Retrofit {
        return retrofit
    }
}

