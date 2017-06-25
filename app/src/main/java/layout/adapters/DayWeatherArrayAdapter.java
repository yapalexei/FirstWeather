package layout.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.claypot.afront.firstweather.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import darksky.weather.response.DayWeatherR;

/**
 * Created by afront on 5/25/17.
 */

public class DayWeatherArrayAdapter extends ArrayAdapter<DayWeatherR> {

    public DayWeatherArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DayWeatherR> objects) {
        super(context, resource, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DayWeatherR dw = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.day, parent, false);
        }

        TextView dayLetter = (TextView) convertView.findViewById(R.id.day_letter);
        dayLetter.setText(getDayOfWeek(dw.getTime()));

        ImageView icon = (ImageView) convertView.findViewById(R.id.weather_icon);
        Drawable weatherIcon = getContext().getDrawable(getIcon(dw.getIcon()));
        icon.setImageDrawable(weatherIcon);

        TextView hiTemp = (TextView) convertView.findViewById(R.id.hi_temp);
        hiTemp.setText(dw.getTemperatureMax() + "");

        TextView loTemp = (TextView) convertView.findViewById(R.id.lo_temp);
        loTemp.setText(dw.getTemperatureMin() + "");

        return convertView;
    }

    public String getDayOfWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        Date day = new Date(time*1000);
        calendar.setTime(day);
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dow) {
            case Calendar.SUNDAY:
                return "Sun";
            case Calendar.MONDAY:
                return "Mon";
            case Calendar.TUESDAY:
                return "Tue";
            case Calendar.WEDNESDAY:
                return "Wed";
            case Calendar.THURSDAY:
                return "Thu";
            case Calendar.FRIDAY:
                return "Fri";
            case Calendar.SATURDAY:
                return "Sat";
            default:
                return "";
        }
    }
    /*
        clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day,
        or partly-cloudy-night -- hail, thunderstorm, or tornado
     */
    public int getIcon(String iconName) {
        int drawableIconId;
        switch (iconName) {
            case "clear-day":
                drawableIconId = R.drawable.sunny_hollow;
                break;
            case "clear-night":
                drawableIconId = R.drawable.clear_night_hollow;
                break;
            case "rain":
                drawableIconId = R.drawable.rainy_hollow;
                break;
            case "snow":
                drawableIconId = R.drawable.snowy_hollow;
                break;
            case "sleet":
                drawableIconId = R.drawable.sleet_hollow;
                break;
            case "wind":
                drawableIconId = R.drawable.windy;
                break;
            case "fog":
                drawableIconId = R.drawable.foggy;
                break;
            case "cloudy":
                drawableIconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                drawableIconId = R.drawable.partly_sunny_hollow;
                break;
            case "partly-cloudy-night":
                drawableIconId = R.drawable.cloudy_night_hollow;
                break;
            case "hail":
                drawableIconId = R.drawable.sleet_hollow;
                break;
            case "thunderstorm":
                drawableIconId = R.drawable.thunder_clouds_hollow;
                break;
            case "tornado":
                drawableIconId = R.drawable.n_a;
                break;
            default:
                drawableIconId = R.drawable.n_a;
        }
        return drawableIconId;
    }
}
