package com.claypot.afront.firstweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
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
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.style.functions.stops.IdentityStops;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;

import java.util.ArrayList;
import java.util.Date;

import darksky.weather.response.CurrentWeatherR;
import darksky.weather.response.DailyWeatherR;
import darksky.weather.response.DayWeatherR;
import darksky.weather.response.WeatherR;
import layout.DayListFragment;

import static com.mapbox.mapboxsdk.style.layers.Filter.eq;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class MainActivity extends AppCompatActivity implements DayListFragment.OnFragmentInteractionListener {
    public static String API = "https://api.darksky.net/";
    private static final int MINUTES = 15;
    private static final int WEATHER_EXPIRES_IN_SEC = 60 * MINUTES;
    public static final int PERMISSION_REQUEST_GPS_CODE = 0;
    private LocationManager locationManager;
    private int orientation;
    private static final String LIFECYCLE_TAG = "ACTIVITY_LIFECYCLE";
    SensorEventListener m_sensorEventListener;
    private MapView mapView;
    private Location curLocation = null;
    private MapboxMap curMap = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        //TODO: save state on sqlite or something
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //TODO: restore state from sqlite or something
        Log.d(LIFECYCLE_TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LIFECYCLE_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token)); // this has to be set BEFORE the layout containing the map gets set/loaded

        setContentView(R.layout.activity_main);

        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Week");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Week");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Map");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Map");
        host.addTab(spec);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if (curMap != null) {
                    Log.d("TAB_INTERACTION", "onTabChanged: " + tabId);
                    if(tabId.equals("Week")) {
                        Log.d("TAB_INTERACTION", "Weather tab");
                    }
                    if(tabId.equals("Map")) {
                        Log.d("TAB_INTERACTION", "Map tab");
                        CameraPosition position;
                        if (curLocation != null) {
                            Log.d("MAPBOX", "onTabChanged: curLocation: " + curLocation.toString());
//                            position = new CameraPosition.Builder().target(new LatLng(curLocation)).zoom(9.0).build();
                            position = new CameraPosition.Builder().target(new LatLng(45.520389, -122.671327)).zoom(17.0).build();
                        } else {
                            Log.d("MAPBOX", "onTabChanged: curLocation is null");
                            position = new CameraPosition.Builder().target(new LatLng(45.520389, -122.671327)).zoom(9.0).build();
                        }

                        curMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                    }
                }
            }});
//        //Tab 3
//        spec = host.newTabSpec("Minutely");
//        spec.setContent(R.id.tab3);
//        spec.setIndicator("Minutely");
//        host.addTab(spec);

        // Mapbox code


        // Create a mapView
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        loadMap();

        Location loc = getLocation();
        curLocation = loc;
        if (loc != null) {
            getWeatherData(loc);
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

    private void loadMap() {
        // Add a MapboxMap
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                Log.d("MAPBOX", "loaded map");
                curMap = mapboxMap;
//                FillLayer fillsLayer = new FillLayer("fills", "population");
//                fillsLayer.setSourceLayer("outgeojson");
//                fillsLayer.setFilter(Filter.all(Filter.lt("pkm2", 300000)));
//                fillsLayer.withProperties(
//                        fillColor(Function.property("pkm2", exponential(
//                                stop(0, fillColor(Color.parseColor("#160e23"))),
//                                stop(14500, fillColor(Color.parseColor("#00617f"))),
//                                stop(145000, fillColor(Color.parseColor("#55e9ff"))))
//                                .withBase(1f)))
//                );
//                mapboxMap.addLayerBelow(fillsLayer, "water");

                // 5.1.0-beta
                FillExtrusionLayer fillExtrusionLayer = new FillExtrusionLayer("3d-buildings", "composite");
                fillExtrusionLayer.setSourceLayer("building");
                fillExtrusionLayer.setFilter(eq("extrude", "true"));
                fillExtrusionLayer.setProperties(
                        fillExtrusionColor(Color.LTGRAY),
                        fillExtrusionHeight(Function.property("height", new IdentityStops<Float>())),
                        fillExtrusionBase(Function.property("min_height", new IdentityStops<Float>())),
                        fillExtrusionOpacity(0.6f)
                );
//                fillExtrusionLayer.withProperties(
//                        fillExtrusionColor(Function.property("pkm2", exponential(
//                                stop(0, fillColor(Color.parseColor("#160e23"))),
//                                stop(14500, fillColor(Color.parseColor("#00617f"))),
//                                stop(145000, fillColor(Color.parseColor("#55e9ff"))))
//                                .withBase(1f))),
//                        fillExtrusionBase(0f),
//                        fillExtrusionHeight(Function.property("pkm2", exponential(
//                                stop(0, fillExtrusionHeight(0f)),
//                                stop(1450000, fillExtrusionHeight(20000f)))
//                                .withBase(1f))));
                mapboxMap.addLayerBelow(fillExtrusionLayer, "airport-label");

            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_GPS_CODE);
        }

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
        Log.d(LIFECYCLE_TAG, "onResume");
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onRestart() {
        Log.d(LIFECYCLE_TAG, "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d(LIFECYCLE_TAG, "onPause");
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(LIFECYCLE_TAG, "onStop");
        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sm.unregisterListener(m_sensorEventListener);
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        Log.d("ACTIVITY_LIFECYCLE", "onDestroy");
        super.onDestroy();
        mapView.onDestroy();
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
            new WeatherStorageTasks(this, weather, WeatherStorageTasks.ACTION_TYPE.PUT, null).execute();
//            new WeatherStorageTasks(this, weather, WeatherStorageTasks.ACTION_TYPE.PUT, new TaskCompleteCallback() {
//                @Override
//                public void onFetchCompleted(WeatherR weather) {
//    //                Log.d("WEATHER", weather.toString());
//    //                updateWeatherUI(weather);
//                }
//            }).execute();
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
