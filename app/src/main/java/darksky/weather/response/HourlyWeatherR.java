package darksky.weather.response;

import java.util.List;

/**
 * Created by afront on 5/8/17.
 */

public class HourlyWeatherR extends BasicWeatherR {
    List<HourWeatherR> data;

    public List<HourWeatherR> getData() {
        return data;
    }

    public void setData(List<HourWeatherR> data) {
        this.data = data;
    }
}
