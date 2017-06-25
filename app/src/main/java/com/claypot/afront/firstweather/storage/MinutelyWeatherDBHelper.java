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

import darksky.weather.response.MinuteWeatherR;

import static darksky.weather.response.Constants.PRECIP_INTENSITY;
import static darksky.weather.response.Constants.PRECIP_PROBABILITY;
import static darksky.weather.response.Constants.TIME;

/**
 * Created by afront on 6/3/17.
 */

public class MinutelyWeatherDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "minutely_weather";

    private static MinutelyWeatherDBHelper sInstance;

    public static synchronized MinutelyWeatherDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new MinutelyWeatherDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    
    private MinutelyWeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<Col> cols = new ArrayList<>();
        cols.add(new Col(PRECIP_INTENSITY, "NUMERIC"));
        cols.add(new Col(PRECIP_PROBABILITY, "NUMERIC"));
        // add all common table columns using custom util
        String TABLE_CREATE = CommonWeatherDBHelper.createTableWithCommonCols(DATABASE_NAME, cols);

        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
            onCreate(db);
        }
    }

    public void putAllWeather(List<MinuteWeatherR> weather) {
        ListIterator<MinuteWeatherR> items = weather.listIterator();
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

    public void putWeather(MinuteWeatherR mw, SQLiteDatabase db) {
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(PRECIP_INTENSITY, mw.getPrecipIntensity());
            values.put(PRECIP_PROBABILITY, mw.getPrecipProbability());
            values.put(TIME, mw.getTime());
            db.insertOrThrow(DATABASE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(CommonWeatherDBHelper.SQLITE_TAG, "Error while trying to add minute weather to database");
        } finally {
            db.endTransaction();
        }
    }

    // TODO: Finish!
    // needs a limit on what to get. Like a start and a finish data or something.
    public List<MinuteWeatherR> getAllWeather() {
        List<MinuteWeatherR> weather = new ArrayList<>();
        String QUERY =
                String.format("SELECT * FROM %s ", DATABASE_NAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    MinuteWeatherR mw = new MinuteWeatherR();

                    mw.setPrecipIntensity(cursor.getColumnIndex(PRECIP_INTENSITY));
                    mw.setPrecipProbability(cursor.getColumnIndex(PRECIP_PROBABILITY));
                    mw.setTime(cursor.getColumnIndex(TIME));

                    weather.add(mw);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(CommonWeatherDBHelper.SQLITE_TAG, "Error while trying to get minute weather from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return weather;
    }
}
