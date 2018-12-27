package projects.jaseem.weatherapp.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;
    private static ApiInterface apiInterface;

    //Retrofit singleton
    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder().baseUrl("https://api.darksky.net/forecast/594f62206c79837632f5ae2182a4c59e/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiInterface = instance.create(ApiInterface.class);
        }
        return instance;
    }
}
