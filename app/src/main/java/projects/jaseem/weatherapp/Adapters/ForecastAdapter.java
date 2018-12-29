package projects.jaseem.weatherapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import projects.jaseem.weatherapp.R;
import projects.jaseem.weatherapp.Retrofit.Models.DataPoint;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private Context mContext;
    private List<DataPoint> forecastList;

    public ForecastAdapter(Context mContext, List<DataPoint> forecastList) {
        this.mContext = mContext;
        this.forecastList = forecastList;
    }

    //Viewholder for forecast cells
    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivForecastIcon;
        private TextView tvForecastDay, tvForecastSummary;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            ivForecastIcon = itemView.findViewById(R.id.iv_forecast_icon);
            tvForecastDay = itemView.findViewById(R.id.tv_forecast_day);
            tvForecastSummary = itemView.findViewById(R.id.tv_forecast_summary);
        }
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_forecast, viewGroup, false);
        return new ForecastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder forecastViewHolder, int i) {
        //Set forecast icon for each day according to server response
        switch (forecastList.get(i).getIcon()) {
            case "clear-day":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.clear_day);
                break;
            case "clear-night":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.clear_night);
                break;
            case "rain":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.rain_day);
                break;
            case "snow":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.snow);
                break;
            case "sleet":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.snow);
                break;
            case "wind":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.wind_day);
                break;
            case "fog":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.fog);
                break;
            case "cloudy":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.cloudy_day);
                break;
            case "partly-cloudy-day":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.cloudy_day);
                break;
            case "partly-cloudy-night":
                forecastViewHolder.ivForecastIcon.setImageResource(R.mipmap.cloudy_night);
                break;
        }

        //Get day of week to display
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        Date date = new Date(forecastList.get(i).getTime() * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //Set texts for day of week and summary of day forecast
        forecastViewHolder.tvForecastDay.setText(sdf.format(calendar.getTime()));
        forecastViewHolder.tvForecastSummary.setText(forecastList.get(i).getSummary());
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }
}
