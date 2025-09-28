package com.pinslog.ww.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale

@Module
@InstallIn(SingletonComponent::class)
class GeocoderModule {
    @Provides
    fun providerGeocoder(@ApplicationContext context: Context) = Geocoder(context, Locale.KOREA)
}