package com.example.minglesports

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.minglesports.presentation.ui.theme.MingleSportsTheme
import com.example.minglesports.presentation.ui.theme.PrimaryThemeColor
import com.google.accompanist.pager.ExperimentalPagerApi

class Splash : AppCompatActivity() {
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MingleSportsTheme() {
                ShowSplash()
            }
        }
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}

@Composable
fun ShowSplash() {
    Column(Modifier.background(PrimaryThemeColor)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            ) {
            Image(
                painterResource(R.drawable.logo2),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp),
            )
        }

    }
}