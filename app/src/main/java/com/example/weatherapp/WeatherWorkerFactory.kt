package com.example.weatherapp

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class WeatherWorkerFactory(
    private val weatherRepository: WeatherRepository,
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            WeatherUpdateWorker::class.java.name ->
                WeatherUpdateWorker(weatherRepository, appContext, workerParameters)
            else -> null
        }
    }
}
