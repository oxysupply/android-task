package com.example.weatherapp


class WeatherRepository {

    private var _weather: Weather? = null
    val weather: Weather? get() = _weather
    var observer: (Weather) -> Unit = {}

    fun setWeather(weather: Weather) {
        _weather = weather
        observer.invoke(weather)
    }
}
