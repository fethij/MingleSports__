package com.example.minglesports.domain.repository

import com.example.minglesports.domain.model.Airport

interface AirportRepository {
    suspend fun getAirports(): List<Airport>
}