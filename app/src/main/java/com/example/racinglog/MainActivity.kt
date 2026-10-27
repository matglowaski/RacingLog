package com.example.racinglog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.racinglog.navigation.AppNavigation
import com.example.racinglog.ui.theme.RacingLogTheme
import com.google.firebase.Firebase
import com.google.firebase.app
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.racinglog.navigation.AppNavigation
import com.example.racinglog.ui.theme.RacingLogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RacingLogTheme {
                AppNavigation()

            }
        }
    }
}





