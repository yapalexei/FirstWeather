package darksky.weather.response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by afront on 5/8/17.
 */

public interface WeatherAPI {
    //37.8267,-122.4233
    @GET("/forecast/a8d5465e232e3f91637697095df995ab/{lat},{lon}")
    Call<WeatherR> getWeather(@Path("lat") double lat, @Path("lon") double lon);
}
