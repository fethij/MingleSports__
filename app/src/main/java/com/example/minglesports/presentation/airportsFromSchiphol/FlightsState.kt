package com.example.minglesports.presentation.airportsFromSchiphol

import com.example.minglesports.domain.model.Flight

data class FlightsState(
    val isLoading: Boolean = false,
    val flights: List<Flight> = emptyList(),
    val error: String = ""
)
