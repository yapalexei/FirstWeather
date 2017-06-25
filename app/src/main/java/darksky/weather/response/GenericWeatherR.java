package darksky.weather.response;

import java.io.Serializable;

/**
 * Created by afront on 5/12/17.
 */

public class GenericWeatherR extends BasicWeatherR implements Serializable {

    private long time;
    private float dewPoint;
    private float humidity;
    private float windSpeed;
    private float windBearing;
    private float visibility;
    private float cloudCover;
    private float pressure;
    private float ozone;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(float dewPoint) {
        this.dewPoint = dewPoint;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindBearing() {
        return windBearing;
    }

    public void setWindBearing(float windBearing) {
        this.windBearing = windBearing;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public float getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(float cloudCover) {
        this.cloudCover = cloudCover;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getOzone() {
        return ozone;
    }

    public void setOzone(float ozone) {
        this.ozone = ozone;
    }
}
