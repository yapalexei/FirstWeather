package com.claypot.afront.firstweather.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import darksky.weather.response.DayWeatherR;
import darksky.weather.response.GenericWeatherR;

import static darksky.weather.response.Constants.APPARENT_TEMPERATURE_MAX;
import static darksky.weather.response.Constants.APPARENT_TEMPERATURE_MAX_TIME;
import static darksky.weather.response.Constants.APPARENT_TEMPERATURE_MIN;
import static darksky.weather.response.Constants.APPARENT_TEMPERATURE_MIN_TIME;
import static darksky.weather.response.Constants.CLOUDCOVER;
import static darksky.weather.response.Constants.DEWPOINT;
import static darksky.weather.response.Constants.HUMIDITY;
import static darksky.weather.response.Constants.ICON;
import static darksky.weather.response.Constants.MOON_PHASE;
import static darksky.weather.response.Constants.OZONE;
import static darksky.weather.response.Constants.PRECIP_INTENSITY;
import static darksky.weather.response.Constants.PRECIP_INTENSITY_MAX;
import static darksky.weather.response.Constants.PRECIP_PROBABILITY;
import static darksky.weather.response.Constants.PRESSURE;
import static darksky.weather.response.Constants.SUMMARY;
import static darksky.weather.response.Constants.SUNRISE_TIME;
import static darksky.weather.response.Constants.SUNSET_TIME;
import static darksky.weather.response.Constants.TEMPERATURE_MAX;
import static darksky.weather.response.Constants.TEMPERATURE_MAX_TIME;
import static darksky.weather.response.Constants.TEMPERATURE_MIN;
import static darksky.weather.response.Constants.TEMPERATURE_MIN_TIME;
import static darksky.weather.response.Constants.TIME;
import static darksky.weather.response.Constants.VISIBILITY;
import static darksky.weather.response.Constants.WINDBEARING;
import static darksky.weather.response.Constants.WINDSPEED;


/**
 * Created by afront on 6/3/17.
 */

public class DailyWeatherDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "daily_weather";

    private static DailyWeatherDBHelper sInstance;

    public static synchronized DailyWeatherDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DailyWeatherDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DailyWeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public void createTable(SQLiteDatabase db) {
        List<Col> cols = new ArrayList<>();
        cols.add(new Col(APPARENT_TEMPERATURE_MAX, "NUMERIC"));
        cols.add(new Col(APPARENT_TEMPERATURE_MAX_TIME, "NUMERIC"));
        cols.add(new Col(APPARENT_TEMPERATURE_MIN, "NUMERIC"));
        cols.add(new Col(APPARENT_TEMPERATURE_MIN_TIME, "NUMERIC"));
        cols.add(new Col(MOON_PHASE, "NUMERIC"));
        cols.add(new Col(PRECIP_INTENSITY, "NUMERIC"));
        cols.add(new Col(PRECIP_INTENSITY_MAX, "NUMERIC"));
        cols.add(new Col(PRECIP_PROBABILITY, "NUMERIC"));
        cols.add(new Col(SUNRISE_TIME, "NUMERIC"));
        cols.add(new Col(SUNSET_TIME, "NUMERIC"));
        cols.add(new Col(TEMPERATURE_MAX, "NUMERIC"));
        cols.add(new Col(TEMPERATURE_MAX_TIME, "NUMERIC"));
        cols.add(new Col(TEMPERATURE_MIN, "NUMERIC"));
        cols.add(new Col(TEMPERATURE_MIN_TIME, "NUMERIC"));
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

    public void putAllWeather(List<DayWeatherR> weather) {
        ListIterator<DayWeatherR> items = weather.listIterator();
        SQLiteDatabase db = getWritableDatabase();
        clearDb(db);
        do {
            putWeather(items.next(), db);
        } while(items.hasNext());
    }
    private void clearDb(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
    private void putWeather(DayWeatherR w, SQLiteDatabase db) {

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

            values.put(PRECIP_PROBABILITY, w.getPrecipProbability());
            values.put(PRECIP_INTENSITY, w.getPrecipIntensity());
            values.put(APPARENT_TEMPERATURE_MAX, w.getApparentTemperatureMax());
            values.put(APPARENT_TEMPERATURE_MAX_TIME, w.getApparentTemperatureMaxTime());
            values.put(APPARENT_TEMPERATURE_MIN, w.getApparentTemperatureMin());
            values.put(APPARENT_TEMPERATURE_MIN_TIME, w.getApparentTemperatureMinTime());
            values.put(PRECIP_INTENSITY_MAX, w.getPrecipIntensityMax());
            values.put(MOON_PHASE, w.getMoonPhase());
            values.put(SUNRISE_TIME, w.getSunriseTime());
            values.put(SUNSET_TIME, w.getSunsetTime());
            values.put(TEMPERATURE_MAX, w.getTemperatureMax());
            values.put(TEMPERATURE_MAX_TIME, w.getTemperatureMaxTime());
            values.put(TEMPERATURE_MIN, w.getTemperatureMin());
            values.put(TEMPERATURE_MIN_TIME, w.getTemperatureMinTime());
            db.insertOrThrow(DATABASE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(CommonWeatherDBHelper.SQLITE_TAG, "Error while trying to add hour weather to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<DayWeatherR> getAllWeather() {
        List<DayWeatherR> weather = new ArrayList<>();
        String QUERY = String.format("SELECT * FROM %s ", DATABASE_NAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    GenericWeatherR gw = CommonWeatherDBHelper.setCommonCols(cursor);

                    DayWeatherR w = new DayWeatherR(gw);
                    w.setPrecipProbability(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            PRECIP_PROBABILITY)));
                    w.setPrecipIntensity(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            PRECIP_INTENSITY)));
                    w.setApparentTemperatureMax(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            APPARENT_TEMPERATURE_MAX)));
                    w.setApparentTemperatureMaxTime(
                            cursor.getLong(
                                    cursor.getColumnIndex(
                                            APPARENT_TEMPERATURE_MAX_TIME)));
                    w.setApparentTemperatureMin(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            APPARENT_TEMPERATURE_MIN)));
                    w.setApparentTemperatureMinTime(
                            cursor.getLong(
                                    cursor.getColumnIndex(
                                            APPARENT_TEMPERATURE_MIN_TIME)));
                    w.setPrecipIntensityMax(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            PRECIP_INTENSITY_MAX)));
                    w.setMoonPhase(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            MOON_PHASE)));
                    w.setSunriseTime(
                            cursor.getLong(
                                    cursor.getColumnIndex(
                                            SUNRISE_TIME)));
                    w.setSunsetTime(
                            cursor.getLong(
                                    cursor.getColumnIndex(
                                            SUNSET_TIME)));
                    w.setTemperatureMax(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            TEMPERATURE_MAX)));
                    w.setTemperatureMaxTime(
                            cursor.getLong(
                                    cursor.getColumnIndex(
                                            TEMPERATURE_MAX_TIME)));
                    w.setTemperatureMin(
                            cursor.getFloat(
                                    cursor.getColumnIndex(
                                            TEMPERATURE_MIN)));
                    w.setTemperatureMinTime(
                            cursor.getLong(
                                    cursor.getColumnIndex(
                                            TEMPERATURE_MIN_TIME)));

                    weather.add(w);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(CommonWeatherDBHelper.SQLITE_TAG, "Error while trying to hour weather from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return weather;
    }
}
