package darksky.weather.response;

import java.util.List;


/**
 * Created by afront on 5/8/17.
 */

public class MinutelyWeatherR extends BasicWeatherR {

    private List<MinuteWeatherR> data;

    public List<MinuteWeatherR> getData() {
        return data;
    }

    public void setData(List<MinuteWeatherR> data) {
        this.data = data;
    }
}
