package com.claypot.afront.firstweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;

import com.claypot.afront.firstweather.async.TaskCompleteCallback;
import com.claypot.afront.firstweather.async.WeatherAPITasks;
import com.claypot.afront.firstweather.async.WeatherStorageTasks;

import java.util.ArrayList;
import java.util.Date;

import darksky.weather.response.CurrentWeatherR;
import darksky.weather.response.DailyWeatherR;
import darksky.weather.response.DayWeatherR;
import darksky.weather.response.WeatherR;
import layout.DayListFragment;

public class MainActivity extends AppCompatActivity implements DayListFragment.OnFragmentInteractionListener {
    public static String API = "https://api.darksky.net/";
    private static final int MINUTES = 15;
    private static final int WEATHER_EXPIRES_IN_SEC = 60 * MINUTES;
    public static final int PERMISSION_REQUEST_GPS_CODE = 0;
    private LocationManager locationManager;
    private int orientation;
    SensorEventListener m_sensorEventListener;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: save state on sqlite or something
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //TODO: restore state from sqlite or something
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ACTIVITY_LIFECYCLE", "onCreate");
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.activity_main);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Week");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Week");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Hourly");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Hourly");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Minutely");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Minutely");
        host.addTab(spec);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_GPS_CODE);
            }

        } else {
            Location loc = getLocation();
            if (loc != null) {
                getWeatherData(loc);
            }
        }
        if (m_sensorEventListener == null) {
            m_sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    int or;
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        or = 0;
                    } else {
                        or = 1;
                    }
                    if(or != orientation) {
                        orientation = or;
                        Log.i("ROTATION","Orientation - " + orientation);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {}
            };
            SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
            sm.registerListener(m_sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_GPS_CODE: {
                getWeatherData(getLocation());
            }
        }

    }

    private void getWeatherData(Location loc) {
        getLocallyStoredWeather(loc);
    }
    private void getRemoteApiWeather(Location loc) {
        try {
            Log.d("API_CALL", "getting new weather data");
            new WeatherAPITasks(loc, new TaskCompleteCallback() {
                @Override
                public void onFetchCompleted(WeatherR weather) {
                    Log.d("API_WEATHER", weather.getTimezone());
                    saveData(weather);
                    updateWeatherUI(weather);
                }
            }).execute();
        } catch (Exception e) {
            Log.d("API_CALL", e.toString());
        }
    }

    private void getLocallyStoredWeather(final Location loc) {
        try {
            new WeatherStorageTasks(this, null, WeatherStorageTasks.ACTION_TYPE.GET, new TaskCompleteCallback() {
                @Override
                public void onFetchCompleted(WeatherR weather) {
                    Log.d("LOCAL_WEATHER", weather.toString());
                    updateWeatherUI(weather);
                    // if there is no weather yet then go get it.
                    if (weather != null) {
                        // check if it's old
                        Date now = new Date();
                        Date expDate = new Date((weather.getCurrently().getTime() + WEATHER_EXPIRES_IN_SEC)*1000);
                        if (expDate.before(now)) {
                            getRemoteApiWeather(loc);
                        }
                    } else {
                        getRemoteApiWeather(loc);
                    }
                }
            }).execute();
        } catch (Exception e) {
            Log.d("STORAGE_DATA", e.toString());
        }
    }

    private Location getLocation() {
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = null;
        try {
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        } catch (SecurityException se) {
            Log.d("LOCATION_ACTION", "could not get GPS location. do we have permission?");
        }
        return lastKnownLocation;
    }

    @Override
    protected void onResume() {
        Log.d("ACTIVITY_LIFECYCLE", "onResume");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.d("ACTIVITY_LIFECYCLE", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d("ACTIVITY_LIFECYCLE", "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("ACTIVITY_LIFECYCLE", "onStop");
        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(m_sensorEventListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("ACTIVITY_LIFECYCLE", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(int index) {
        //TODO: do some cool stuff when clicking list item.
        Log.d("TAG", "Main Activity log ----> " + index);
//        Intent intent = new Intent(this, DayDetailsActivity.class);
//        intent.putExtra("DAY_POINT", recentWeatherRes.getDaily().getData().get(index));
//        startActivityForResult(intent, DAY_POINT_RESPONSE);
    }

    private void saveData(WeatherR weather) {
        try {
            new WeatherStorageTasks(this, weather, WeatherStorageTasks.ACTION_TYPE.PUT, new TaskCompleteCallback() {
                @Override
                public void onFetchCompleted(WeatherR weather) {
    //                Log.d("WEATHER", weather.toString());
    //                updateWeatherUI(weather);
                }
            }).execute();
        } catch (Exception e) {
            Log.d("STORAGE_DATA", e.toString());
        }
    }

    private void updateWeatherUI(WeatherR weather) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        CurrentWeatherR currentWeather = weather.getCurrently();


        DailyWeatherR dayWeather = weather.getDaily();
        updateDayWeather(dayWeather, fragmentManager);
    }

    private void updateCurrentWeather(CurrentWeatherR currentWeather, FragmentManager fragmentManager) {
//        CurrentWeatherFragment currentWeatherFrag = (CurrentWeatherFragment)fragmentManager.findFragmentById(R.id.current_weather_frag);
//
//        if (currentWeatherFrag != null) {
//            currentWeatherFrag.setWeather(currentWeather);
//        }
    }

    private void updateDayWeather(DailyWeatherR dayWeather, FragmentManager fragmentManager) {
        DayListFragment dayList = (DayListFragment) fragmentManager.findFragmentById(R.id.day_list_frag);

        if (dayList != null) {
            dayList.replaceList((ArrayList<DayWeatherR>) dayWeather.getData());
            Log.d("AddingItems", "Adding and item to list");
        } else {
            Log.d("Error", "for some reason the dayList fragment is null!");
        }
    }
}
