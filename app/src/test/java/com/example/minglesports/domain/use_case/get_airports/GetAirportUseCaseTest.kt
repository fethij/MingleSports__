package com.example.minglesports.domain.use_case.get_airports

import com.example.minglesports.common.Constants
import com.example.minglesports.domain.model.Airport
import com.example.minglesports.domain.model.Flight
import com.example.minglesports.domain.model.FlightsDistanceModel
import com.example.minglesports.domain.use_case.get_flights.GetFlightUseCase
import com.example.minglesports.repository.FakeAirportRepository
import com.example.minglesports.repository.FakeFlightRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class GetAirportUseCaseTest {

    private lateinit var getAirportUseCase: GetAirportUseCase
    private lateinit var getFlightUseCase: GetFlightUseCase

    private lateinit var fakeAirportRepository: FakeAirportRepository
    private lateinit var fakeFlightRepository: FakeFlightRepository

    private val airportsToInsert = mutableListOf<Airport>()
    private val flightsToInsert = mutableListOf<Flight>()

    @Before
    fun setUp() {
        fakeAirportRepository = FakeAirportRepository()
        fakeFlightRepository = FakeFlightRepository()


        getAirportUseCase = GetAirportUseCase(fakeAirportRepository)
        getFlightUseCase = GetFlightUseCase(fakeFlightRepository)


        val airport1 = Airport(
            city = "Rotterdam",
            countryId = "NL",
            id = "RTM",
            latitude = 51.948948,
            longitude = 4.433606,
            name = "Rotterdam The Hague Airport"
        )

        val airport2 = Airport(
            city = "Brussels",
            countryId = "BE",
            id = "BRU",
            latitude = 50.89717,
            longitude = 4.483602,
            name = "Brussels Airport"
        )

        val airport3 = Airport(
            city = "Dusseldorf",
            countryId = "DE",
            id = "DUS",
            latitude = 51.278328,
            longitude = 6.76558,
            name = "Dusseldorf Airport"
        )

        airportsToInsert.add(airport1)
        airportsToInsert.add(airport2)
        airportsToInsert.add(airport3)

        val flight1 = Flight(
            airlineId = "HV",
            flightNumber = 553,
            departureAirportId = "AMS",
            arrivalAirportId = "RTM",
        )

        val flight2 = Flight(
            airlineId = "WA",
            flightNumber = 1721,
            departureAirportId = "AMS",
            arrivalAirportId = "BRU",
        )

        val flight3 = Flight(
            airlineId = "WA",
            flightNumber = 1853,
            departureAirportId = "AMS",
            arrivalAirportId = "DUS",
        )

        flightsToInsert.add(flight1)
        flightsToInsert.add(flight2)
        flightsToInsert.add(flight3)

    }

    @Test
    fun `Order airports by distance from Schiphol`() = runBlocking {
        //Schiphol Airport Coordinates
        val lat1 = Constants.SCHIPHOL_LAT
        val long2 = Constants.SCHIPHOL_LONG

        val distances: ArrayList<FlightsDistanceModel> = ArrayList()

        for (flight in flightsToInsert) {
            for (airport in airportsToInsert) {
                if (flight.departureAirportId == Constants.SCHIPHOL
                    && flight.arrivalAirportId == airport.id
                ) {
                    if (distance(
                            lat1,
                            long2,
                            airport.latitude,
                            airport.longitude
                        ) < 80000.0
                    ) {
                        val model = FlightsDistanceModel(
                            airport = airport,
                            distance = distance(
                                lat1,
                                long2,
                                airport.latitude,
                                airport.longitude
                            )
                        )
                        distances.add(model)
                    }
                }
            }
        }

        val sortedList = distances.sortedWith(compareBy { it.distance })
        for (i in 0..sortedList.size - 2) {
            assertThat(sortedList[i].distance).isLessThan(sortedList[i + 1].distance)
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist = (dist * 100.0).roundToInt() / 100.0
        return dist;
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}