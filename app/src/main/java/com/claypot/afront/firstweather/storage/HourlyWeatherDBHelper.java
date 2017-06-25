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

import darksky.weather.response.GenericWeatherR;
import darksky.weather.response.HourWeatherR;

import static darksky.weather.response.Constants.APPARENT_TEMPERATURE;
import static darksky.weather.response.Constants.CLOUDCOVER;
import static darksky.weather.response.Constants.DEWPOINT;
import static darksky.weather.response.Constants.HUMIDITY;
import static darksky.weather.response.Constants.ICON;
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

public class HourlyWeatherDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hourly_weather";

    private static HourlyWeatherDBHelper sInstance;

    public static synchronized HourlyWeatherDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new HourlyWeatherDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private HourlyWeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public void createTable(SQLiteDatabase db) {
        List<Col> cols = new ArrayList<>();
        cols.add(new Col(APPARENT_TEMPERATURE, "NUMERIC"));
        cols.add(new Col(PRECIP_INTENSITY, "NUMERIC"));
        cols.add(new Col(PRECIP_PROBABILITY, "NUMERIC"));
        cols.add(new Col(TEMPERATURE, "NUMERIC"));
        String TABLE_CREATE = CommonWeatherDBHelper.createTableWithCommonCols(DATABASE_NAME, cols);

        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
            onCreate(db);
        }
    }

    public void putAllWeather(List<HourWeatherR> weather) {
        ListIterator<HourWeatherR> items = weather.listIterator();
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
    private void putWeather(HourWeatherR w, SQLiteDatabase db) {
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

            values.put(APPARENT_TEMPERATURE, w.getApparentTemperature());
            values.put(PRECIP_INTENSITY, w.getPrecipIntensity());
            values.put(PRECIP_PROBABILITY, w.getPrecipProbability());
            values.put(TEMPERATURE, w.getTemperature());

            db.insertOrThrow(DATABASE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(CommonWeatherDBHelper.SQLITE_TAG, "Error while trying to add hour weather to database");
        } finally {
            db.endTransaction();
        }
    }

    // TODO: Finish!
    // needs a limit on what to get. Like a start and a finish data or something.
    public List<HourWeatherR> getAllWeather() {
        List<HourWeatherR> weather = new ArrayList<>();
        String QUERY = String.format("SELECT * FROM %s ", DATABASE_NAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    GenericWeatherR gw = CommonWeatherDBHelper.setCommonCols(cursor);

                    HourWeatherR w = new HourWeatherR(gw) ;
                    w.setApparentTemperature(cursor.getColumnIndex(APPARENT_TEMPERATURE));
                    w.setPrecipIntensity(cursor.getColumnIndex(PRECIP_INTENSITY));
                    w.setPrecipProbability(cursor.getColumnIndex(PRECIP_PROBABILITY));
                    w.setTemperature(cursor.getColumnIndex(TEMPERATURE));

                    weather.add(w);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(CommonWeatherDBHelper.SQLITE_TAG, e.toString());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return weather;
    }
}
