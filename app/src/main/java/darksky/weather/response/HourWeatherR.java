package darksky.weather.response;

import java.io.Serializable;

/**
 * Created by afront on 5/8/17.
 */

public class HourWeatherR extends GenericWeatherR implements Serializable {

    private float precipIntensity;
    private float precipProbability;
    private float temperature;
    private float apparentTemperature;

    public HourWeatherR(GenericWeatherR gw) {
        // assign the extended props from GenericWeatherR.class because one can't cast a superclass to a subclass
        this.setSummary(gw.getSummary());
        this.setIcon(gw.getIcon());
        this.setTime(gw.getTime());
        this.setDewPoint(gw.getDewPoint());
        this.setHumidity(gw.getHumidity());
        this.setWindSpeed(gw.getWindSpeed());
        this.setWindBearing(gw.getWindBearing());
        this.setVisibility(gw.getVisibility());
        this.setCloudCover(gw.getCloudCover());
        this.setPressure(gw.getPressure());
        this.setOzone(gw.getOzone());
    }

    public float getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(float precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public float getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(float precipProbability) {
        this.precipProbability = precipProbability;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(float apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }
}
