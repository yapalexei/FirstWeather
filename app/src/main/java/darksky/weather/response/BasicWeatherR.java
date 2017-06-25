package darksky.weather.response;

import java.io.Serializable;

/**
 * Created by afront on 6/23/17.
 */

public class BasicWeatherR implements Serializable {
    private String summary;
    private String icon;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
