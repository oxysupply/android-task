package com.example.weatherapp


class WeatherRepository {

    var weather: Weather? = null
        private set
    var observer: (Weather) -> Unit = {}

    fun setWeather(weather: Weather) {
        this.weather = weather
        observer.invoke(weather)
    }
}
