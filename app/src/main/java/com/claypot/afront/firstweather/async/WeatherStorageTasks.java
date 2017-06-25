package com.claypot.afront.firstweather.async;

import android.content.Context;
import android.os.AsyncTask;

import com.claypot.afront.firstweather.storage.CurrentWeatherDBHelper;
import com.claypot.afront.firstweather.storage.DailyWeatherDBHelper;
import com.claypot.afront.firstweather.storage.HourlyWeatherDBHelper;
import com.claypot.afront.firstweather.storage.MinutelyWeatherDBHelper;

import java.util.List;

import darksky.weather.response.CurrentWeatherR;
import darksky.weather.response.DailyWeatherR;
import darksky.weather.response.DayWeatherR;
import darksky.weather.response.HourWeatherR;
import darksky.weather.response.HourlyWeatherR;
import darksky.weather.response.MinuteWeatherR;
import darksky.weather.response.MinutelyWeatherR;
import darksky.weather.response.WeatherR;

/**
 * Created by afront on 6/24/17.
 */

public class WeatherStorageTasks extends AsyncTask<Object, Object, WeatherR> {
    public enum ACTION_TYPE {
        GET,
        PUT
    }
    public enum WEATHER_TYPE {
        ALL,
        DAILY,
        HOURLY,
        MINUTELY,
        CURRENT
    }
    private Context activityContext = null;
    public TaskCompleteCallback delegate = null;
    private ACTION_TYPE action = null;
    private WeatherR thisWeather = null;
    public WeatherStorageTasks(Context con, WeatherR weather, ACTION_TYPE aType, TaskCompleteCallback callback) throws Exception {
        activityContext = con;
        action = aType;
        this.delegate = callback;
        thisWeather = weather;
    }


    /**
     * All the saving happens here.
     */
    private void putAllWeather(WeatherR weather) {
        putCurWeather(weather.getCurrently());
        putDayWeather(weather.getDaily().getData());
        putHourWeather(weather.getHourly().getData());
        putMinuteWeather(weather.getMinutely().getData());
    }

    private void putCurWeather(CurrentWeatherR weather) {
        // TODO: Finish!!
        CurrentWeatherDBHelper table = CurrentWeatherDBHelper.getInstance(activityContext);
        table.putAllWeather(weather);
    }

    private void putDayWeather(List<DayWeatherR> weather) {
        DailyWeatherDBHelper table = DailyWeatherDBHelper.getInstance(activityContext);
        table.putAllWeather(weather);
    }

    private void putHourWeather(List<HourWeatherR> weather) {
        HourlyWeatherDBHelper table = HourlyWeatherDBHelper.getInstance(activityContext);
        table.putAllWeather(weather);
    }

    private void putMinuteWeather(List<MinuteWeatherR> weather) {
        MinutelyWeatherDBHelper table = MinutelyWeatherDBHelper.getInstance(activityContext);
        table.putAllWeather(weather);
    }

    /**
     * All the GET methods
     */
    private WeatherR getAllWeather() {
        return new WeatherR(getCurWeather(), getMinuteWeather(), getHourWeather(), getDayWeather());
    }


    private CurrentWeatherR getCurWeather() {
        // TODO: Finish!!
        CurrentWeatherDBHelper table = CurrentWeatherDBHelper.getInstance(activityContext);
        CurrentWeatherR weather = table.getAllWeather();
        return weather;
    }

    private DailyWeatherR getDayWeather() {
        DailyWeatherDBHelper table = DailyWeatherDBHelper.getInstance(activityContext);
        DailyWeatherR weather = new DailyWeatherR();
        weather.setData(table.getAllWeather());
        return weather;
    }

    private HourlyWeatherR getHourWeather() {
        HourlyWeatherDBHelper table = HourlyWeatherDBHelper.getInstance(activityContext);
        HourlyWeatherR weather = new HourlyWeatherR();
        weather.setData(table.getAllWeather());
        return weather;
    }

    private MinutelyWeatherR getMinuteWeather() {
        MinutelyWeatherDBHelper table = MinutelyWeatherDBHelper.getInstance(activityContext);
        MinutelyWeatherR weather = new MinutelyWeatherR();
        weather.setData(table.getAllWeather());
        return weather;
    }

    @Override
    protected WeatherR doInBackground(Object... objects) {
        switch (action) {
            case PUT:
                putAllWeather(thisWeather);
                break;
            case GET:
                // do nothing because we always return something;
                break;
        }
        return getAllWeather();
    }

    @Override
    protected void onPostExecute(WeatherR weather) {
        delegate.onFetchCompleted(weather);
    }
}
