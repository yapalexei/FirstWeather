package darksky.weather.response;

/**
 * Created by afront on 5/8/17.
 */

public class WeatherR {
//    public static final int CURRENT_POINT_RESPONSE = 100;
//    public static final int MINUTE_POINT_RESPONSE = 200;
//    public static final int HOUR_POINT_RESPONSE = 300;
//    public static final int DAY_POINT_RESPONSE = 400;
    private float latitude;
    private float longitude;
    private String timezone;
    private int offset;
    private CurrentWeatherR currently;
    private MinutelyWeatherR minutely;
    private HourlyWeatherR hourly;
    private DailyWeatherR daily;

    public WeatherR(CurrentWeatherR currently,
                    MinutelyWeatherR minutely,
                    HourlyWeatherR hourly,
                    DailyWeatherR daily) {
        this.currently = currently;
        this.minutely = minutely;
        this.hourly = hourly;
        this.daily = daily;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public CurrentWeatherR getCurrently() {
        return currently;
    }

    public void setCurrently(CurrentWeatherR currently) {
        this.currently = currently;
    }

    public MinutelyWeatherR getMinutely() {
        return minutely;
    }

    public void setMinutely(MinutelyWeatherR minutely) {
        this.minutely = minutely;
    }

    public HourlyWeatherR getHourly() {
        return hourly;
    }

    public void setHourly(HourlyWeatherR hourly) {
        this.hourly = hourly;
    }

    public DailyWeatherR getDaily() {
        return daily;
    }

    public void setDaily(DailyWeatherR daily) {
        this.daily = daily;
    }
}
