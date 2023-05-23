package com.pinslog.ww.di

import com.pinslog.ww.data.data_source.WeatherRemoteDataSource
import com.pinslog.ww.data.data_source.impl.WeatherRemoteDataSourceImpl
import com.pinslog.ww.domain.repository.WeatherRepository
import com.pinslog.ww.domain.repository.impl.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl): WeatherRepository {
        return WeatherRepositoryImpl(weatherRemoteDataSourceImpl)
    }
}