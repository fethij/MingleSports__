package com.example.minglesports.presentation.airportMapList

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minglesports.R
import com.example.minglesports.common.Resource
import com.example.minglesports.domain.model.Airport
import com.example.minglesports.domain.model.NearestAirportModel
import com.example.minglesports.domain.use_case.get_airports.GetAirportUseCase
import com.google.android.libraries.maps.model.BitmapDescriptor
import com.google.android.libraries.maps.model.BitmapDescriptorFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class AirportMapListViewModel @Inject constructor(
    private val getAirportUseCase: GetAirportUseCase
) : ViewModel() {

    private val _state = mutableStateOf(AirportMapListState())
    val state: State<AirportMapListState> = _state

    private var _furthestAirport = mutableListOf<Airport>()
    val furthestAirports: List<Airport> = _furthestAirport

    var far1: Airport? by mutableStateOf(null)
    var far2: Airport? by mutableStateOf(null)

    init {
        getAirports()
    }
    private fun getAirports() {
        getAirportUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = AirportMapListState(airports = result.data ?: emptyList())
                    furthestAirport(result.data!!)
                }
                is Resource.Error -> {
                    _state.value = AirportMapListState(
                        error = (result.message ?: R.string.httpException) as String
                    )
                }
                is Resource.Loading -> {
                    _state.value = AirportMapListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun selectedAirport(latitude: Double, longitude: Double): Airport? {
        val airports = state.value.airports
        for (airport in airports) {
            if (latitude == airport.latitude && longitude == airport.longitude) {
                return airport
            }
        }
        return null
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun furthestAirport(dataList: List<Airport>) {
        _furthestAirport.clear()
        var furthest = 0.0
        for (airport1 in dataList!!) {
            for (airport2 in dataList!!) {
                if (distance(
                        airport1.latitude,
                        airport1.longitude,
                        airport2.latitude,
                        airport2.longitude
                    ) > furthest
                ) {
                    far1 = airport1
                    far2 = airport2
                    furthest = distance(
                        airport1.latitude,
                        airport1.longitude,
                        airport2.latitude,
                        airport2.longitude
                    )
                }
            }
        }
        _furthestAirport.add(far1!!)
        _furthestAirport.add(far2!!)
    }

    fun nearestAirport(latitude: Double, longitude: Double, context: Context): NearestAirportModel {

        val nearestAirport = NearestAirportModel("", 1000000000000000.0)

        for (airport in state.value.airports!!) {
            if (latitude == airport.latitude
                && longitude == airport.longitude){
            } else if (distance(
                    latitude,
                    longitude,
                    airport.latitude,
                    airport.longitude
                ) < nearestAirport.AirportDistance
            ) {
                nearestAirport.AirportDistance =
                    distance(latitude, longitude, airport.latitude, airport.longitude)
                nearestAirport.AirportName = airport.name
            }
        }
        return nearestAirport
    }

    //https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude/16794680#16794680
    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = Math.round(dist * 100.0) / 100.0
        return dist;
    }

    //This function converts decimal degrees to radians
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    //This function converts radians to decimal degrees
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}