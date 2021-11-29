package com.example.minglesports.repository

import com.example.minglesports.domain.model.Flight
import com.example.minglesports.domain.repository.FlightRepository

class FakeFlightRepository : FlightRepository {

    private val flights = mutableListOf<Flight>()
    override suspend fun getFlights(): List<Flight> {
        return flights
    }
}