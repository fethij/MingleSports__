package com.example.minglesports.presentation.Settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minglesports.MainActivity
import com.example.minglesports.R
import com.example.minglesports.common.Constants
import com.example.minglesports.presentation.ui.theme.PrimaryThemeColor
import com.example.minglesports.presentation.ui.theme.WhiteColor
import com.google.accompanist.pager.ExperimentalPagerApi
import java.util.*

@ExperimentalPagerApi
@Composable
fun SettingTab(context: Context) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryThemeColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        //Language
        val sharedPreferences1: SharedPreferences = context.getSharedPreferences(
            stringResource(id = R.string.language),
            Context.MODE_PRIVATE
        )
        val lng =
            sharedPreferences1.getString(stringResource(id = R.string.language), Constants.ENGLISH)
        val countriesList = mutableListOf<String>(Constants.ENGLISH, Constants.DUTCH)

        // State variables
        var lngName: String by remember { mutableStateOf(lng.toString()) }
        var expanded1 by remember { mutableStateOf(false) }
        Column(
            Modifier.fillMaxSize()
        ) {
            Box()
            {

                Row {
                    Row(
                        Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.settingLanguage),
                            color = WhiteColor,
                            fontSize = 24.sp
                        )
                    }
                    Spacer(Modifier.width(55.dp))
                    Row(
                        Modifier
                            .padding(24.dp)
                            .clickable {
                                expanded1 = !expanded1
                            },
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top
                    ) {

                        // Anchor view
                        Text(
                            text = lngName,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        ) // Country name label
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")

                        //
                        DropdownMenu(expanded = expanded1, onDismissRequest = {
                            expanded1 = false
                        }) {
                            countriesList.forEach { language ->
                                DropdownMenuItem(onClick = {
                                    expanded1 = false
                                    lngName = language

                                    val sharedPreferences: SharedPreferences =
                                        context.getSharedPreferences(
                                            Constants.LNG,
                                            Context.MODE_PRIVATE
                                        )
                                    var lng = ""
                                    if (language.equals(Constants.ENGLISH)) {
                                        val editor: SharedPreferences.Editor =
                                            sharedPreferences.edit()
                                        editor.putString(Constants.LNG, Constants.ENGLISH)
                                        editor.apply()
                                        lng = Constants.ENG
                                    } else {
                                        val editor: SharedPreferences.Editor =
                                            sharedPreferences.edit()
                                        editor.putString(Constants.LNG, Constants.DUTCH)
                                        editor.apply()
                                        lng = Constants.NL
                                    }

                                    val locale = Locale(lng)
                                    Locale.setDefault(locale)
                                    val config: Configuration =
                                        context.getResources().getConfiguration()
                                    config.locale = locale
                                    context.getResources().updateConfiguration(
                                        config,
                                        context.getResources().getDisplayMetrics()
                                    )
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                    val activity = (context as? Activity)
                                    activity?.finish()
                                }) {
                                    Text(text = language)
                                }
                            }
                        }
                    }
                }
            }

            //Km to Mile
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                stringResource(id = R.string.distanceSP),
                Context.MODE_PRIVATE
            )
            val distance = sharedPreferences.getString(
                stringResource(id = R.string.distanceSP),
                stringResource(id = R.string.km)
            )
            val disList = mutableListOf<String>(
                stringResource(id = R.string.km),
                stringResource(id = R.string.miles)
            )

            // State variables
            var disName: String by remember { mutableStateOf(distance.toString()) }
            var expanded by remember { mutableStateOf(false) }

            Box() {
                Row() {
                    Row(Modifier.padding(24.dp))
                    {
                        Text(
                            text = stringResource(id = R.string.measurement),
                            color = WhiteColor,
                            fontSize = 24.sp
                        )
                    }
                    Spacer(Modifier.width(30.dp))
                    Row(
                        Modifier
                            .padding(24.dp)
                            .clickable {
                                expanded = !expanded
                            }
                    ) { // Anchor view
                        Text(
                            text = disName,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        ) // Country name label
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")

                        //
                        DropdownMenu(expanded = expanded, onDismissRequest = {
                            expanded = false
                        }) {
                            disList.forEach { dis ->
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    disName = dis

                                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                    editor.putString(Constants.DIS, dis)
                                    editor.apply()


                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                    val activity = (context as? Activity)
                                    activity?.finish()


                                }) {
                                    Text(text = dis)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}