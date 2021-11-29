package com.example.minglesports.presentation.airportsFromSchiphol

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.minglesports.R
import com.example.minglesports.domain.model.Airport
import com.example.minglesports.domain.model.FlightsDistanceModel
import com.example.minglesports.presentation.airportMapList.AirportMapListViewModel
import com.example.minglesports.presentation.ui.theme.PrimaryLightThemeColor
import com.example.minglesports.presentation.ui.theme.PrimaryThemeColor
import com.example.minglesports.presentation.ui.theme.WhiteColor

@Composable
fun FlightsTab(
    context: Context,
    FlyViewModel: AirportsFromSchipholViewModel = hiltViewModel(),
    AirportViewModel: AirportMapListViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        getFlights(
            airportViewModel = AirportViewModel,
            flyViewModel = FlyViewModel,
            context
        )
    }
}

@Composable
fun getFlights(
    airportViewModel: AirportMapListViewModel,
    flyViewModel: AirportsFromSchipholViewModel,
    context: Context
) {

    airportViewModel.state.value.airports
    val airportsList: List<Airport> = airportViewModel.state.value.airports
    FlightList(list = flyViewModel.getSortedList(airportsList), context = context)

}

@Composable
fun FlightList(list: List<FlightsDistanceModel>, context: Context) {
    LazyColumn {
        Modifier.fillMaxSize()
        items(list.size) { flight ->
            Box(Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
                FlightRow(
                    flight,
                    list,
                    context
                )
            }
        }
    }
}

@Composable
fun FlightRow(flight: Int, list: List<FlightsDistanceModel>, context: Context) {
    Row(
        Modifier
            .fillMaxSize()
            .background(PrimaryThemeColor),
    ) {
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(10),
            elevation = 2.dp,
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(PrimaryLightThemeColor)
                    .padding(10.dp)
            ) {
                Column() {
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_airplanemode_active_24),
                            "",
                            tint = WhiteColor
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Text(
                            text = list[flight].airport.name,
                            color = WhiteColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
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
                        Text(text = list[flight].airport.city, color = WhiteColor)
                    }
                    Spacer(modifier = Modifier.padding(5.dp))

                    val sharedPreferences: SharedPreferences =
                        context.getSharedPreferences(
                            stringResource(id = R.string.distanceSP),
                            Context.MODE_PRIVATE
                        )
                    val dis = sharedPreferences.getString(
                        stringResource(id = R.string.distanceSP),
                        stringResource(id = R.string.km)
                    )
                    if (dis.equals(stringResource(id = R.string.km))) {
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_connecting_airports_24),
                                "",
                                tint = WhiteColor
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(
                                text = Math.round(list[flight].distance).toString()
                                        + " "
                                        + stringResource(id = R.string.km)
                                        + " "
                                        + stringResource(id = R.string.fromSchiphol),
                                color = WhiteColor
                            )
                        }
                    } else {
                        Row() {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_connecting_airports_24),
                                "",
                                tint = WhiteColor
                            )
                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(
                                text = Math.round((list[flight].distance * 0.621371)).toString()
                                        + " "
                                        + stringResource(id = R.string.miles)
                                        + " "
                                        + stringResource(id = R.string.fromSchiphol),
                                color = WhiteColor
                            )
                        }
                    }
                }
            }
        }
    }
}
