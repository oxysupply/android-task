package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class MainActivity : ComponentActivity() {

    private lateinit var weatherRepository: WeatherRepository
    private val weatherUpdateApi = WeatherUpdateApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                var weather by remember { mutableStateOf<Weather?>(null) }
                LaunchedEffect(true) {
                    weatherRepository.observer = { weather = it }
                    CoroutineScope(Dispatchers.IO).launch {
                        while (isActive) {
                            try {
                                val newWeather = weatherUpdateApi.fetchWeather()
                                newWeather?.let(weatherRepository::setWeather)
                            } finally {
                                delay(15.minutes)
                            }
                        }
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Surface {
                                    weather?.windSpeed?.let {
                                        when {
                                            it < 0 -> R.drawable.ic_snowflake
                                            it < 5 -> R.drawable.ic_cloud_sun
                                            else -> R.drawable.ic_sun
                                        }.let {
                                            Icon(
                                                painter = painterResource(id = it),
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                            )
                                        }
                                    }
                                    val temperatureText = when (weather?.temperature) {
                                        null -> "???°C"
                                        else -> "%.1f°C".format(weather!!.temperature)
                                    }
                                    Text(text = temperatureText)
                                }
                                val windDirectionText = when (weather?.windDirection) {
                                    null -> "Wind direction: ???"
                                    else -> "Wind direction: %d".format(weather!!.windDirection)
                                }
                                Text(text = windDirectionText)
                                val windSpeedText = when (weather?.windSpeed) {
                                    null -> "Wind speed: ??? m/s"
                                    else -> "Wind speed: %.2f m/s".format(weather!!.windSpeed)
                                }
                                Text(text = windSpeedText)
                            }
                        }

                    }
                }
            }
        }
    }
}
