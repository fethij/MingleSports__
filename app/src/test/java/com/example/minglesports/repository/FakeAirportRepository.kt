package com.example.minglesports.repository

import com.example.minglesports.domain.model.Airport
import com.example.minglesports.domain.repository.AirportRepository

class FakeAirportRepository : AirportRepository {

    private val airports = mutableListOf<Airport>()

    override suspend fun getAirports(): List<Airport> {
        return airports
    }

}