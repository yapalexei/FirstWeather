package com.claypot.afront.firstweather.async;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import darksky.weather.response.WeatherAPI;
import darksky.weather.response.WeatherR;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afront on 6/23/17.
 * TODO: https://stackoverflow.com/questions/9963691/android-asynctask-sending-callbacks-to-ui
 */
public class WeatherAPITasks extends AsyncTask<Object, Object, Call<WeatherR>> {
    public static String API = "https://api.darksky.net/";
    private Double lat;
    private Double lon;
    public TaskCompleteCallback delegate = null;

    public WeatherAPITasks(Location loc, TaskCompleteCallback callback) throws Exception {
        this.delegate = callback;
        if(loc != null) {
            this.lat = loc.getLatitude();
            this.lon = loc.getLongitude();
        } else {
            throw new Exception("Location cannot be null");
        }
    }

    @Override
    protected void onPreExecute() {
        if (this.lat == null || this.lon == null) {

        }
        Log.d("API_CALL", "onPreExecute");
        super.onPreExecute();
    }

    @Override
    protected Call<WeatherR> doInBackground(Object... strings) {

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weather = retrofit.create(WeatherAPI.class);
        System.out.println(weather.getWeather(this.lat, this.lon));
        Call<WeatherR> call = weather.getWeather(this.lat, this.lon);

        return call;
    }

    @Override
    protected void onPostExecute(Call<WeatherR> call) {
        call.enqueue(new Callback<WeatherR>() {
            @Override
            public void onResponse(Call<WeatherR> call, Response<WeatherR> response) {
                Log.d("API_CALL", "onResponse - got a response");
                delegate.onFetchCompleted(response.body());
            }

            @Override
            public void onFailure(Call<WeatherR> call, Throwable t) {
                Log.d("API_CALL", "Fail!");
            }
        });
    }

}
