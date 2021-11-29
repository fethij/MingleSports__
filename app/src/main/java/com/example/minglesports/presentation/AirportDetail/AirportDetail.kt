package com.example.minglesports.presentation.AirportDetail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minglesports.R
import com.example.minglesports.domain.model.Airport
import com.example.minglesports.domain.model.NearestAirportModel
import com.example.minglesports.presentation.airportMapList.AirportMapListViewModel
import com.example.minglesports.presentation.airportMapList.components.rememberMapViewWithLifecycle
import com.example.minglesports.presentation.ui.theme.MingleSportsTheme
import com.example.minglesports.presentation.ui.theme.PrimaryLightThemeColor
import com.example.minglesports.presentation.ui.theme.PrimaryThemeColor
import com.example.minglesports.presentation.ui.theme.WhiteColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagerApi
class AirPortDetail : AppCompatActivity() {

    private val airportMapListViewModel: AirportMapListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val latitude: Double = intent.getDoubleExtra(R.string.lat.toString(), 0.0)
            val longitude: Double = intent.getDoubleExtra(R.string.lon.toString(), 0.0)
            MingleSportsTheme {
                AirportDetailContent(
                    this,
                    latitude,
                    longitude,
                    viewModel = airportMapListViewModel
                )
            }
        }
    }
}

@Composable
private fun AirportDetailContent(
    context: Context,
    latitude: Double,
    longitude: Double,
    viewModel: AirportMapListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val airport: Airport? = viewModel.selectedAirport(latitude = latitude, longitude = longitude)
    if (airport != null) {
        val nearestAirport: NearestAirportModel =
            viewModel.nearestAirport(latitude = latitude, longitude = longitude, context = context)
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(PrimaryThemeColor)
        ) {
            Surface(
                modifier = Modifier
                    .weight(3.5f)
                    .padding(10.dp),
                RoundedCornerShape(10),
            ) {
                AirportMapView(airport.latitude, airport.longitude, context, viewModel)
            }
            Surface(
                modifier = Modifier
                    .weight(3f)
                    .padding(10.dp),
                RoundedCornerShape(10)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PrimaryLightThemeColor)
                        .padding(20.dp)
                        .verticalScroll(scrollState)

                ) {
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_airplanemode_active_24),
                            "",
                            tint = WhiteColor
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = airport.name,
                            color = WhiteColor,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_location_on_24),
                            "",
                            tint = WhiteColor
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = airport.id,
                            color = WhiteColor
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_location_city_24),
                            "",
                            tint = WhiteColor
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = airport.city,
                            color = WhiteColor
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_flag_24),
                            "",
                            tint = WhiteColor
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = airport.countryId,

                            color = WhiteColor
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_pin_drop_24),
                            "",
                            tint = WhiteColor
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = airport.latitude.toString(),
                            color = WhiteColor
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_pin_drop_24),
                            "",
                            tint = WhiteColor
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = airport.longitude.toString(),
                            color = WhiteColor
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    val sharedPreferences: SharedPreferences =
                        context.getSharedPreferences(
                            stringResource(id = R.string.distanceSP),
                            Context.MODE_PRIVATE
                        )
                    var dis = sharedPreferences.getString(
                        stringResource(id = R.string.distanceSP),
                        stringResource(id = R.string.distanceSP)
                    )
                    if (dis.equals(stringResource(id = R.string.km))) {
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_connecting_airports_24),
                                "",
                                tint = WhiteColor
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Column() {
                                Text(
                                    text = stringResource(id = R.string.NearestAirport) + " " + nearestAirport.AirportName,
                                    color = WhiteColor
                                )
                                Text(
                                    text = stringResource(id = R.string.Distance) + " " + Math.round(
                                        nearestAirport.AirportDistance
                                    ) + " " + stringResource(
                                        id = R.string.km
                                    ),
                                    color = WhiteColor
                                )
                            }
                        }
                    } else {
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_connecting_airports_24),
                                "",
                                tint = WhiteColor
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Column() {
                                Text(
                                    text = stringResource(id = R.string.NearestAirport) + " " + nearestAirport.AirportName,
                                    color = WhiteColor
                                )
                                Text(
                                    text = stringResource(id = R.string.Distance)
                                            + " "
                                            + Math.round((nearestAirport.AirportDistance * 0.621371) * 100.0 / 100)
                                            + " "
                                            + stringResource(id = R.string.miles),
                                    color = WhiteColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AirportMapView(
    latitude: Double,
    longitude: Double,
    context: Context,
    viewModel: AirportMapListViewModel
) {
    // The MapView lifecycle is handled by this composable. As the MapView also needs to be updated
    // with input from Compose UI, those updates are encapsulated into the MapViewContainer
    // composable. In this way, when an update to the MapView happens, this composable won't
    // recompose and the MapView won't need to be recreated.
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, latitude, longitude, context, viewModel)

}

@Composable
@OptIn(ExperimentalPagerApi::class)
private fun MapViewContainer(
    map: MapView,
    latitude: Double,
    longitude: Double,
    context: Context,
    viewModel: AirportMapListViewModel
) {
    val airport: Airport? = viewModel.selectedAirport(latitude = latitude, longitude = longitude)
    val coroutineScope = rememberCoroutineScope()
    AndroidView({ map }) { mapView ->
        // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
        // is stored for later, Compose doesn't recognize state reads
        coroutineScope.launch {
            val googleMap = mapView.awaitMap()
            // Zoom Controls in map
            googleMap.uiSettings.isZoomControlsEnabled = true

            if (airport != null) {
                googleMap.addMarker {

                    if (!viewModel.furthestAirports.contains(airport)) {
                        position(LatLng(airport.latitude, airport.longitude))
                            .title(airport.name)
                            .snippet(airport.city)
                            .draggable(true)
                            .icon(
                                viewModel.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_baseline_local_airport_45
                                )
                            )
                    } else {
                        position(LatLng(airport.latitude, airport.longitude))
                            .title(airport.name)
                            .snippet(airport.city)
                            .draggable(true)
                            .icon(
                                viewModel.bitmapDescriptorFromVector(
                                    context,
                                    R.drawable.ic_baseline_local_airport_55
                                )
                            )
                    }
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                airport.latitude,
                                airport.longitude
                            )
                        )
                    )
                }
            }
        }
    }
}