package com.pinslog.ww.di

import com.pinslog.ww.domain.repository.WeatherRepository
import com.pinslog.ww.domain.repository.impl.WeatherRepositoryImpl
import com.pinslog.ww.domain.usecase.WeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideWeatherUseCase(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherUseCase {
        return WeatherUseCase(weatherRepositoryImpl)
    }
}