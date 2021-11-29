package com.example.minglesports.domain.model

data class Flight(
    val airlineId : String,
    val flightNumber : Int,
    val departureAirportId : String,
    val arrivalAirportId : String,
)