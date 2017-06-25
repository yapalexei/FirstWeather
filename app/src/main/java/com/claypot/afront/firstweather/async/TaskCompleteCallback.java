package com.claypot.afront.firstweather.async;

import darksky.weather.response.WeatherR;

/**
 * Created by afront on 6/24/17.
 */

public interface TaskCompleteCallback {
    void onFetchCompleted(WeatherR weather);
}
