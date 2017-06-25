package com.claypot.afront.firstweather.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import darksky.weather.response.CurrentWeatherR;

import static darksky.weather.response.Constants.APPARENT_TEMPERATURE;
import static darksky.weather.response.Constants.CLOUDCOVER;
import static darksky.weather.response.Constants.DEWPOINT;
import static darksky.weather.response.Constants.HUMIDITY;
import static darksky.weather.response.Constants.ICON;
import static darksky.weather.response.Constants.NEAREST_STORM_BEARING;
import static darksky.weather.response.Constants.NEAREST_STORM_DISTANCE;
import static darksky.weather.response.Constants.OZONE;
import static darksky.weather.response.Constants.PRECIP_INTENSITY;
import static darksky.weather.response.Constants.PRECIP_PROBABILITY;
import static darksky.weather.response.Constants.PRESSURE;
import static darksky.weather.response.Constants.SUMMARY;
import static darksky.weather.response.Constants.TEMPERATURE;
import static darksky.weather.response.Constants.TIME;
import static darksky.weather.response.Constants.VISIBILITY;
import static darksky.weather.response.Constants.WINDBEARING;
import static darksky.weather.response.Constants.WINDSPEED;

/**
 * Created by afront on 6/3/17.
 */

public class CurrentWeatherDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "current_weather";

    private static CurrentWeatherDBHelper sInstance;

    public static synchronized CurrentWeatherDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new CurrentWeatherDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private CurrentWeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public void createTable(SQLiteDatabase db) {
        List<Col> cols = new ArrayList<>();
        cols.add(new Col(NEAREST_STORM_DISTANCE, "NUMERIC"));
        cols.add(new Col(NEAREST_STORM_BEARING, "NUMERIC"));
        cols.add(new Col(PRECIP_INTENSITY, "NUMERIC"));
        cols.add(new Col(PRECIP_PROBABILITY, "NUMERIC"));
        cols.add(new Col(TEMPERATURE, "NUMERIC"));
        cols.add(new Col(APPARENT_TEMPERATURE, "NUMERIC"));
        String TABLE_CREATE = CommonWeatherDBHelper.createTableWithCommonCols(DATABASE_NAME, cols);
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            clearDb(db);
        }
    }

    private void clearDb(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public void putAllWeather(CurrentWeatherR w) {
        SQLiteDatabase db = getWritableDatabase();
        clearDb(db);
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(CLOUDCOVER, w.getCloudCover());
            values.put(DEWPOINT, w.getDewPoint());
            values.put(HUMIDITY, w.getHumidity());
            values.put(ICON, w.getIcon());
            values.put(OZONE, w.getOzone());
            values.put(PRESSURE, w.getPressure());
            values.put(SUMMARY, w.getSummary());
            values.put(VISIBILITY, w.getVisibility());
            values.put(WINDBEARING, w.getWindBearing());
            values.put(WINDSPEED, w.getWindSpeed());
            values.put(TIME, w.getTime());

            values.put(NEAREST_STORM_DISTANCE, w.getNearestStormDistance());
            values.put(NEAREST_STORM_BEARING, w.getNearestStormBearing());
            values.put(PRECIP_INTENSITY, w.getPrecipIntensity());
            values.put(PRECIP_PROBABILITY, w.getPrecipProbability());
            values.put(TEMPERATURE, w.getTemperature());
            values.put(APPARENT_TEMPERATURE, w.getApparentTemperature());
            db.insertOrThrow(DATABASE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(CommonWeatherDBHelper.SQLITE_TAG, "Error while trying to add hour weather to database");
        } finally {
            db.endTransaction();
        }
    }

    public CurrentWeatherR getAllWeather() {
        // TODO: Finish!!
        return null;
    }
}
