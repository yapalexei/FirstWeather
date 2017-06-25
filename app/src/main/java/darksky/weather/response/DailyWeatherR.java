package darksky.weather.response;

import java.util.List;

/**
 * Created by afront on 5/8/17.
 */

public class DailyWeatherR extends BasicWeatherR {
    List<DayWeatherR> data;

    public List<DayWeatherR> getData() {
        return data;
    }

    public void setData(List<DayWeatherR> data) {
        this.data = data;
    }
}
