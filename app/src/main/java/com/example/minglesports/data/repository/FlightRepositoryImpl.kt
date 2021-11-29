package com.example.minglesports.data.repository

import com.example.minglesports.data.remote.AirportFlightApi
import com.example.minglesports.domain.model.Flight
import com.example.minglesports.domain.repository.FlightRepository
import javax.inject.Inject

class FlightRepositoryImpl @Inject constructor(
    private val flightApi: AirportFlightApi
) : FlightRepository {
    override suspend fun getFlights(): List<Flight> {
        return flightApi.getFlights()
    }
}