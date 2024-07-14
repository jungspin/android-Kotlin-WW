package com.pinslog.ww.domain.repository.impl

import com.pinslog.ww.data.data_source.impl.WeatherRemoteDataSourceImpl
import com.pinslog.ww.data.model.openweather.WeatherResponse
import com.pinslog.ww.data.model.weatherLatLng.WeatherLatLng
import com.pinslog.ww.domain.repository.WeatherRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

/**
 * WeatherRepository 구현체
 * @author jungspin
 * @since 2023/05/23 10:50 PM
 */

class WeatherRepositoryImpl @Inject constructor(private val weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl) :
    WeatherRepository {

    override fun getCurrentWeatherLatLng(
        lat: Double,
        lon: Double,
    ): Single<WeatherLatLng> {
        return weatherRemoteDataSourceImpl.getCurrentWeatherLatLng(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getForecastLatLng(
        lat: Double,
        lon: Double,
    ): Single<WeatherResponse> {
        return weatherRemoteDataSourceImpl.getForecastLatLng(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Flow<Response<WeatherLatLng>> {
        return weatherRemoteDataSourceImpl.getCurrentWeather(lat, lon)
    }

    override suspend fun getForecastWeather(lat: Double, lon: Double): Flow<Response<WeatherResponse>> {
        return weatherRemoteDataSourceImpl.getForecast(lat, lon)
    }
}