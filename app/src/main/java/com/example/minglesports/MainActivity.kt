package com.example.minglesports

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.minglesports.common.Constants
import com.example.minglesports.presentation.Settings.SettingTab
import com.example.minglesports.presentation.airportMapList.AirportTab
import com.example.minglesports.presentation.airportsFromSchiphol.FlightsTab
import com.example.minglesports.presentation.ui.theme.MingleSportsTheme
import com.example.minglesports.presentation.ui.theme.PrimaryLightThemeColor
import com.example.minglesports.presentation.ui.theme.PrimaryThemeColor
import com.example.minglesports.presentation.ui.theme.WhiteColor
import com.google.accompanist.pager.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@Suppress("DEPRECATION")
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(Constants.LNG,Context.MODE_PRIVATE)
        val lng = sharedPreferences.getString(Constants.LNG,Constants.ENGLISH)
        if (lng.equals(Constants.ENGLISH))
        {
            val locale = Locale(Constants.ENG)
            Locale.setDefault(locale)
            val config: Configuration = resources.configuration
            config.locale = locale
            resources.updateConfiguration(
                config,
                resources.displayMetrics
            )
        }else
        {
            val locale = Locale(Constants.NL)
            Locale.setDefault(locale)
            val config: Configuration = resources.configuration
            config.locale = locale
            resources.updateConfiguration(
                config,
                resources.displayMetrics
            )
        }
        setContent {
            MingleSportsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TabScreen(this)
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabScreen(context: Context) {
    val pagerState = rememberPagerState(pageCount = 3)
    Column(
        modifier = Modifier.background(PrimaryThemeColor)
    ) {
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState,context)
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(stringResource(R.string.tab1), stringResource(R.string.tab2), stringResource(R.string.tab3))
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = PrimaryThemeColor,
        contentColor = PrimaryLightThemeColor,
        divider = {
            TabRowDefaults.Divider(
                thickness = 2.dp,
                color = PrimaryLightThemeColor
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = WhiteColor
            )
        }
    ) {
        list.forEachIndexed { index, _->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) WhiteColor else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, context: Context) {
    HorizontalPager(state = pagerState) { page ->
        when(page) {
            0 -> AirportTab(context)
            1 -> FlightsTab(context)
            2 -> SettingTab(context)
        }
    }
}