package com.claypot.afront.firstweather.storage;

import android.database.Cursor;

import java.util.Iterator;
import java.util.List;

import darksky.weather.response.GenericWeatherR;

import static darksky.weather.response.Constants.CLOUDCOVER;
import static darksky.weather.response.Constants.DEWPOINT;
import static darksky.weather.response.Constants.HUMIDITY;
import static darksky.weather.response.Constants.ICON;
import static darksky.weather.response.Constants.OZONE;
import static darksky.weather.response.Constants.PRESSURE;
import static darksky.weather.response.Constants.SUMMARY;
import static darksky.weather.response.Constants.TIME;
import static darksky.weather.response.Constants.VISIBILITY;
import static darksky.weather.response.Constants.WINDBEARING;
import static darksky.weather.response.Constants.WINDSPEED;

/**
 * Created by afront on 6/3/17.
 */

public class CommonWeatherDBHelper {
    static final String SQLITE_TAG = "SQLITE";
    static final String ID = "id";

    static final String COMMON_TABLE_COLS =
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CLOUDCOVER + " NUMERIC, " +
            DEWPOINT + " NUMERIC, " +
            HUMIDITY + " NUMERIC, " +
            ICON + " VARCHAR(150), " +
            OZONE + " NUMERIC, " +
            PRESSURE + " NUMERIC, " +
            SUMMARY + " TEXT, " +
            TIME + " NUMERIC, " +
            VISIBILITY + " NUMERIC, " +
            WINDBEARING + " NUMERIC, " +
            WINDSPEED + " NUMERIC";

    static String createTableWithCommonCols(String DB, List<Col> cols) {
        String CREATE_TABLE = "CREATE TABLE " + DB + " (" + COMMON_TABLE_COLS;

        final Iterator<Col> iterator = cols.iterator();

        while (iterator.hasNext()){
            Col col = iterator.next();
            CREATE_TABLE += ", " + col.getKey() + " " + col.getVal();
        }

        // finally
        CREATE_TABLE += ")";

        return CREATE_TABLE;
    }

    static GenericWeatherR setCommonCols(Cursor cursor) {
        GenericWeatherR gw = new GenericWeatherR();
        gw.setCloudCover(cursor.getFloat(cursor.getColumnIndex(CLOUDCOVER)));
        gw.setDewPoint(cursor.getFloat(cursor.getColumnIndex(DEWPOINT)));
        gw.setHumidity(cursor.getFloat(cursor.getColumnIndex(HUMIDITY)));
        gw.setIcon(cursor.getString(cursor.getColumnIndex(ICON)));
        gw.setOzone(cursor.getFloat(cursor.getColumnIndex(OZONE)));
        gw.setPressure(cursor.getFloat(cursor.getColumnIndex(PRESSURE)));
        gw.setSummary(cursor.getString(cursor.getColumnIndex(SUMMARY)));
        gw.setVisibility(cursor.getFloat(cursor.getColumnIndex(VISIBILITY)));
        gw.setWindBearing(cursor.getFloat(cursor.getColumnIndex(WINDBEARING)));
        gw.setWindSpeed(cursor.getFloat(cursor.getColumnIndex(WINDSPEED)));
        gw.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
        return gw;
    }
}
