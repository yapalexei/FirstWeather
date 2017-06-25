package darksky.weather.response;

import java.io.Serializable;

/**
 * Created by afront on 5/8/17.
 */

public class DayWeatherR extends GenericWeatherR implements Serializable{

    private long sunriseTime;
    private long sunsetTime;
    private float moonPhase;
    private float precipIntensity;
    private float precipIntensityMax;
    private float precipProbability;
    private float temperatureMin;
    private long temperatureMinTime;
    private float temperatureMax;
    private long temperatureMaxTime;
    private float apparentTemperatureMin;
    private long apparentTemperatureMinTime;
    private float apparentTemperatureMax;
    private long apparentTemperatureMaxTime;

    public DayWeatherR(GenericWeatherR gw) {
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

    public long getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(long sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public long getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(long sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public float getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(float moonPhase) {
        this.moonPhase = moonPhase;
    }

    public float getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(float precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public float getPrecipIntensityMax() {
        return precipIntensityMax;
    }

    public void setPrecipIntensityMax(float precipIntensityMax) {
        this.precipIntensityMax = precipIntensityMax;
    }

    public float getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(float precipProbability) {
        this.precipProbability = precipProbability;
    }

    public float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public float getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public void setTemperatureMinTime(long temperatureMinTime) {
        this.temperatureMinTime = temperatureMinTime;
    }

    public float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public long getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public void setTemperatureMaxTime(long temperatureMaxTime) {
        this.temperatureMaxTime = temperatureMaxTime;
    }

    public float getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public void setApparentTemperatureMin(float apparentTemperatureMin) {
        this.apparentTemperatureMin = apparentTemperatureMin;
    }

    public long getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public void setApparentTemperatureMinTime(long apparentTemperatureMinTime) {
        this.apparentTemperatureMinTime = apparentTemperatureMinTime;
    }

    public float getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public void setApparentTemperatureMax(float apparentTemperatureMax) {
        this.apparentTemperatureMax = apparentTemperatureMax;
    }

    public long getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
    }

    public void setApparentTemperatureMaxTime(long apparentTemperatureMaxTime) {
        this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
    }
}
