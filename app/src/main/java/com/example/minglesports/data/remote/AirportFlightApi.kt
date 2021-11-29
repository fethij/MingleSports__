package com.example.minglesports.data.remote


import com.example.minglesports.domain.model.Airport
import com.example.minglesports.domain.model.Flight
import retrofit2.http.GET

interface AirportFlightApi {
    @GET("airports.json")
    suspend fun getAirports(): List<Airport>

    @GET("flights.json")
    suspend fun getFlights(): List<Flight>
}