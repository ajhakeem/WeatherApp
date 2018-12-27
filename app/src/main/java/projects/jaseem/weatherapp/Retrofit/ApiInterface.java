package projects.jaseem.weatherapp.Retrofit;

import projects.jaseem.weatherapp.Retrofit.Models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("{latlng}")
    Call<WeatherResponse> getWeather(@Path("latlng") String latLng);
}
