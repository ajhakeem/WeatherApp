package projects.jaseem.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import projects.jaseem.weatherapp.Retrofit.Models.WeatherResponse;

public class WeatherFragment extends Fragment {
    private static final String TAG = "WeatherFragment";
    private static final int NIGHT_CODE = 0;
    private static final int DAY_CODE = 1;

    private WeatherResponse weatherResponse;
    private ConstraintLayout clWeather;
    private TextView tvSelectedCity, tvTemperature, tvDescription, tvHumidity, tvPressure, tvChanceOfRain, tvForecast, tvForecastSummary;
    private ImageView ivIcon, ivForecastIcon;
    private String selectedCity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_weather, container, false);

        clWeather = rootLayout.findViewById(R.id.cl_weather);
        tvSelectedCity = rootLayout.findViewById(R.id.tv_selected_city);
        tvTemperature = rootLayout.findViewById(R.id.tv_temperature);
        tvDescription = rootLayout.findViewById(R.id.tv_description);
        tvHumidity = rootLayout.findViewById(R.id.tv_humidity);
        tvPressure = rootLayout.findViewById(R.id.tv_pressure);
        tvChanceOfRain = rootLayout.findViewById(R.id.tv_chance_of_rain);
        tvForecast = rootLayout.findViewById(R.id.tv_forecast);
        tvForecastSummary = rootLayout.findViewById(R.id.tv_forecast_summary);
        ivIcon = rootLayout.findViewById(R.id.iv_weather_icon);
        ivForecastIcon = rootLayout.findViewById(R.id.iv_forecast_icon);

        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 18) {
            toggleViewTheme(NIGHT_CODE);
        } else {
            toggleViewTheme(DAY_CODE);
        }

        updateUI();
        return rootLayout;
    }

    public void setWeatherResponse(WeatherResponse weatherResponse, String selectedCity) {
        this.weatherResponse = weatherResponse;
        this.selectedCity = selectedCity;
    }

    public void updateUI() {
        switch (weatherResponse.getCurrently().getIcon()) {
            case "clear-day":
                ivIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "clear-night":
                ivIcon.setImageResource(R.mipmap.clear_night);
                ivIcon.setBackgroundColor(getResources().getColor(R.color.nightTextColor));
                break;
            case "rain":
                ivIcon.setImageResource(R.mipmap.rain_day);
                break;
            case "snow":
                ivIcon.setImageResource(R.mipmap.snow);
                break;
            case "sleet":
                ivIcon.setImageResource(R.mipmap.snow);
                break;
            case "wind":
                ivIcon.setImageResource(R.mipmap.wind_day);
                break;
            case "fog":
                ivIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "cloudy":
                ivIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "partly-cloudy-day":
                ivIcon.setImageResource(R.mipmap.cloudy_day);
                break;
            case "partly-cloudy-night":
                ivIcon.setImageResource(R.mipmap.cloudy_night);
                break;
        }

        switch (weatherResponse.getHourly().getIcon()) {
            case "clear-day":
                ivForecastIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "clear-night":
                ivForecastIcon.setImageResource(R.mipmap.clear_night);
                break;
            case "rain":
                ivForecastIcon.setImageResource(R.mipmap.rain_day);
                break;
            case "snow":
                ivForecastIcon.setImageResource(R.mipmap.snow);
                break;
            case "sleet":
                ivForecastIcon.setImageResource(R.mipmap.snow);
                break;
            case "wind":
                ivForecastIcon.setImageResource(R.mipmap.wind_day);
                break;
            case "fog":
                ivForecastIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "cloudy":
                ivForecastIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "partly-cloudy-day":
                ivForecastIcon.setImageResource(R.mipmap.cloudy_day);
                break;
            case "partly-cloudy-night":
                ivForecastIcon.setImageResource(R.mipmap.cloudy_night);
                break;
        }

        DecimalFormat df = new DecimalFormat("#");
        int humidityPercentage, chanceOfRain;
        if (weatherResponse.getCurrently().getHumidity() == "1") {
            humidityPercentage = 100;
        } else {
            Double d = Double.parseDouble(weatherResponse.getCurrently().getHumidity());
            humidityPercentage = (int) Math.round(d * 100);
        }

        if (weatherResponse.getCurrently().getPrecipProbability() == "1") {
            chanceOfRain = 100;
        } else {
            Double d = Double.parseDouble(weatherResponse.getCurrently().getPrecipProbability());
            chanceOfRain = (int) Math.round(d * 100);
        }

        tvSelectedCity.setText(selectedCity);
        tvTemperature.setText(getString(R.string.temp_f, String.valueOf(df.format(weatherResponse.getCurrently().getTemperature()))));
        tvDescription.setText(getString(R.string.description, weatherResponse.getCurrently().getSummary()));
        tvHumidity.setText(getString(R.string.humidity, humidityPercentage));
        tvPressure.setText(getString(R.string.pressure, weatherResponse.getCurrently().getPressure()));
        tvChanceOfRain.setText(getString(R.string.precip_chance, chanceOfRain));
        tvForecastSummary.setText(getString(R.string.forecast_summary, weatherResponse.getHourly().getSummary()));
    }

    private void toggleViewTheme(int code) {
        switch (code) {
            case DAY_CODE:
                clWeather.setBackground(getResources().getDrawable(R.drawable.day_gradient));
                tvSelectedCity.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvTemperature.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvDescription.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvHumidity.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvPressure.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvChanceOfRain.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvForecast.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvForecastSummary.setTextColor(getResources().getColor(R.color.dayTextColor));
                ivIcon.setColorFilter(getResources().getColor(R.color.dayTextColor));
                ivForecastIcon.setColorFilter(getResources().getColor(R.color.dayTextColor));
                break;
            case NIGHT_CODE:
                clWeather.setBackground(getResources().getDrawable(R.drawable.night_gradient));
                tvSelectedCity.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvTemperature.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvDescription.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvHumidity.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvPressure.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvChanceOfRain.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvForecast.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvForecastSummary.setTextColor(getResources().getColor(R.color.nightTextColor));
                ivIcon.setColorFilter(getResources().getColor(R.color.nightTextColor));
                ivForecastIcon.setColorFilter(getResources().getColor(R.color.nightTextColor));
                break;
        }
    }

}
