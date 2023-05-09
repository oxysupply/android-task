package com.example.weatherapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class WeatherUpdateWorker(
    private val weatherRepository: WeatherRepository,
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        try {
            val response = withContext(Dispatchers.IO) {
                OkHttpClient().newCall(
                    Request.Builder()
                        .method("GET", null)
                        .url("https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&windspeed_unit=ms")
                        .build()
                ).execute()
            }
            return if (response.isSuccessful) {
                JSONObject(response.body?.string())
                    .getJSONObject("current_weather")
                    .let {
                        val weather = Weather(
                            it.getDouble("temperature"),
                            it.getDouble("windspeed"),
                            it.getInt("winddirection"),
                        )
                        withContext(Dispatchers.Main) {
                            weatherRepository.setWeather(weather)
                        }
                    }
                Result.success()
            } else Result.failure()
        } catch (ex: Exception) {
            return Result.failure()
        }
    }
}
