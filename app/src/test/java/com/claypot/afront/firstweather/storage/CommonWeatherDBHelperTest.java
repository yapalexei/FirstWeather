package com.claypot.afront.firstweather.storage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by afront on 6/4/17.
 */

public class CommonWeatherDBHelperTest {
    @Test
    public void createTableWithCommonCols() {
        String expectedOutput =
                "CREATE TABLE TEST (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "summary TEXT, " +
                        "icon VARCHAR(150), " +
                        "dewPoint NUMERIC, " +
                        "humidity NUMERIC, " +
                        "windSpeed NUMERIC, " +
                        "windBearing NUMERIC, " +
                        "visibility NUMERIC, " +
                        "cloudCover NUMERIC, " +
                        "pressure NUMERIC, " +
                        "ozone NUMERIC)";
        List<Col> cols = new ArrayList<>();
        String res = CommonWeatherDBHelper.createTableWithCommonCols("TEST", cols);
        assertEquals("Result is", expectedOutput, res);

        expectedOutput =
                "CREATE TABLE TEST (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "summary TEXT, " +
                        "icon VARCHAR(150), " +
                        "dewPoint NUMERIC, " +
                        "humidity NUMERIC, " +
                        "windSpeed NUMERIC, " +
                        "windBearing NUMERIC, " +
                        "visibility NUMERIC, " +
                        "cloudCover NUMERIC, " +
                        "pressure NUMERIC, " +
                        "ozone NUMERIC, " +
                        "testCol STRING)";
        cols.add(new Col("testCol", "STRING"));
        res = CommonWeatherDBHelper.createTableWithCommonCols("TEST", cols);
        assertEquals("Result is", expectedOutput, res);
    }
}
