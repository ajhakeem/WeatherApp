package projects.jaseem.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;
import java.util.Locale;

import projects.jaseem.weatherapp.Adapters.ForecastAdapter;
import projects.jaseem.weatherapp.Retrofit.Models.DataBlock;
import projects.jaseem.weatherapp.Retrofit.Models.DataPoint;
import projects.jaseem.weatherapp.Retrofit.Models.WeatherResponse;

public class WeatherFragment extends Fragment {
    private static final String TAG = "WeatherFragment";
    private static final int NIGHT_CODE = 0;
    private static final int DAY_CODE = 1;

    private WeatherResponse weatherResponse;
    private ConstraintLayout clWeather;
    private TextView tvSelectedCity, tvTemperature, tvDescription, tvHumidity, tvPressure, tvChanceOfRain;
    private ImageView ivIcon;
    private String selectedCity;
    private RecyclerView rvForecast;
    private ForecastAdapter forecastAdapter;
    private List<DataPoint> dataPoints;

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
        ivIcon = rootLayout.findViewById(R.id.iv_weather_icon);
        rvForecast = rootLayout.findViewById(R.id.rv_forecast);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvForecast.setLayoutManager(layoutManager);
        forecastAdapter = new ForecastAdapter(getContext(), dataPoints);
        rvForecast.setAdapter(forecastAdapter);

        //Set UI and theme upon fragment coming into view
        updateUI();
        return rootLayout;
    }

    //Public method for activity to set fragment's weather response object, and to parse forecast days to only include future days
    public void setWeatherResponse(WeatherResponse weatherResponse, String selectedCity) {
        this.weatherResponse = weatherResponse;
        this.selectedCity = selectedCity;
        int indexOffset = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        Calendar cal1 = Calendar.getInstance();
        Date date1 = new Date(weatherResponse.getCurrently().getTime() * 1000);
        cal1.setTime(date1);

        for (int i = 0; i < weatherResponse.getDaily().getData().size(); i++) {
            Calendar cal2 = Calendar.getInstance();
            Date date2 = new Date(weatherResponse.getDaily().getData().get(i).getTime() * 1000);
            cal2.setTime(date2);

            boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

            if (sameDay) {
                indexOffset++;
            }
        }

        //Only obtain the days that are in the future of current day
        this.dataPoints = weatherResponse.getDaily().getData().subList(indexOffset, weatherResponse.getDaily().getData().size() - 1);

        if (forecastAdapter != null) {
            forecastAdapter = null;
            forecastAdapter = new ForecastAdapter(getContext(), dataPoints);
            rvForecast.setAdapter(forecastAdapter);
        }
    }

    public void updateUI() {
        //Compare time of request retrieval and sunset time at desired location to determine day or night theme
        if (weatherResponse.getCurrently().getTime() < Long.valueOf(weatherResponse.getDaily().getData().get(0).getSunsetTime())) {
            toggleViewTheme(DAY_CODE);
        } else {
            toggleViewTheme(NIGHT_CODE);
        }

        //Set icon according to weather response for current day
        switch (weatherResponse.getCurrently().getIcon()) {
            case "clear-day":
                ivIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "clear-night":
                ivIcon.setImageResource(R.mipmap.clear_night);
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
                ivIcon.setImageResource(R.mipmap.fog);
                clWeather.setBackground(getResources().getDrawable(R.drawable.cloudy_gradient));
                break;
            case "cloudy":
                ivIcon.setImageResource(R.mipmap.cloudy_day);
                clWeather.setBackground(getResources().getDrawable(R.drawable.cloudy_gradient));
                break;
            case "partly-cloudy-day":
                ivIcon.setImageResource(R.mipmap.cloudy_day);
                clWeather.setBackground(getResources().getDrawable(R.drawable.cloudy_gradient));
                break;
            case "partly-cloudy-night":
                ivIcon.setImageResource(R.mipmap.cloudy_night);
                break;
                default:
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
    }

    private void toggleViewTheme(int code) {
        switch (code) {
            case DAY_CODE:
                clWeather.setBackground(getResources().getDrawable(R.drawable.day_gradient));
                tvSelectedCity.setTextColor(getResources().getColor(R.color.offwhite));
                tvTemperature.setTextColor(getResources().getColor(R.color.offwhite));
                tvDescription.setTextColor(getResources().getColor(R.color.offwhite));
                tvHumidity.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvPressure.setTextColor(getResources().getColor(R.color.dayTextColor));
                tvChanceOfRain.setTextColor(getResources().getColor(R.color.dayTextColor));
                ivIcon.setColorFilter(getResources().getColor(R.color.offwhite));
                break;
            case NIGHT_CODE:
                clWeather.setBackground(getResources().getDrawable(R.drawable.night_gradient));
                tvSelectedCity.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvTemperature.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvDescription.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvHumidity.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvPressure.setTextColor(getResources().getColor(R.color.nightTextColor));
                tvChanceOfRain.setTextColor(getResources().getColor(R.color.nightTextColor));
                ivIcon.setColorFilter(getResources().getColor(R.color.nightTextColor));
                break;
        }
    }



}
