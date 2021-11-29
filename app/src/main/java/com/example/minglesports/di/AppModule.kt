package com.example.minglesports.di

import com.example.minglesports.common.Constants
import com.example.minglesports.data.remote.AirportFlightApi
import com.example.minglesports.data.repository.AirportRepositoryImpl
import com.example.minglesports.data.repository.FlightRepositoryImpl
import com.example.minglesports.domain.repository.AirportRepository
import com.example.minglesports.domain.repository.FlightRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
//This dependencies live as long as the application
@InstallIn(SingletonComponent::class)
object AppModule{
    @Provides
    @Singleton
    fun provideAirportApi(): AirportFlightApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AirportFlightApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAirportRepository(flightApi: AirportFlightApi): AirportRepository{
        return AirportRepositoryImpl(flightApi)
    }

    @Provides
    @Singleton
    fun provideFlightRepository(flightApi: AirportFlightApi): FlightRepository{
        return FlightRepositoryImpl(flightApi)
    }
}