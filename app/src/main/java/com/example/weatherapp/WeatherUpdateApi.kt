package com.example.weatherapp

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class WeatherUpdateApi {

    private val client = HttpClient()

    suspend fun fetchWeather(): Weather? {
        val response = withContext(Dispatchers.IO) {
            client.get("https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&windspeed_unit=ms")
        }
        if (!response.status.isSuccess()) return null
        val currentWeatherJson = response.bodyAsText()
            .let(::JSONObject)
            .getJSONObject("current_weather")
        return Weather(
            currentWeatherJson.getDouble("temperature"),
            currentWeatherJson.getDouble("windspeed"),
            currentWeatherJson.getInt("winddirection"),
        )
    }
}
