package com.pinslog.ww.di

import com.pinslog.ww.data.api.WeatherApi
import com.pinslog.ww.data.data_source.WeatherRemoteDataSource
import com.pinslog.ww.data.data_source.impl.WeatherRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun provideWeatherRemoteDataSource(weatherApi: WeatherApi): WeatherRemoteDataSource = WeatherRemoteDataSourceImpl(weatherApi)
}