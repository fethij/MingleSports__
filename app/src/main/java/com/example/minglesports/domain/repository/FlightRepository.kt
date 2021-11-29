package com.example.minglesports.domain.repository

import com.example.minglesports.domain.model.Flight

interface FlightRepository {
    suspend fun getFlights(): List<Flight>
}