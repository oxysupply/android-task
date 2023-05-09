package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.work.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var weatherRepository: WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherRepository = WeatherRepository()
        Configuration.Builder()
            .setWorkerFactory(WeatherWorkerFactory(weatherRepository))
            .build()
            .let { WorkManager.initialize(this, it) }
        setContent {
            WeatherAppTheme {
                var weather by remember { mutableStateOf<Weather?>(null) }
                LaunchedEffect(true) {
                    weatherRepository.observer = { weather = it }
                    PeriodicWorkRequest
                        .Builder(WeatherUpdateWorker::class.java, 15, TimeUnit.MINUTES)
                        .setConstraints(Constraints(NetworkType.CONNECTED))
                        .addTag("weather")
                        .build()
                        .let {
                            WorkManager.getInstance(applicationContext)
                                .enqueueUniquePeriodicWork(
                                    "weather",
                                    ExistingPeriodicWorkPolicy.UPDATE,
                                    it,
                                )
                        }
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
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
