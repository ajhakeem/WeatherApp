package projects.jaseem.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Locale;

import projects.jaseem.weatherapp.Retrofit.ApiInterface;
import projects.jaseem.weatherapp.Retrofit.Models.WeatherResponse;
import projects.jaseem.weatherapp.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_CODE = 111;

    private FragmentManager mFragmentManager;
    private PlaceAutocompleteFragment mPlaceAutocompleteFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Geocoder mGeocoder;
    private Location lastKnownLocation;
    private Place selectedPlace;
    private StringBuilder sbLatLng = new StringBuilder();
    private Button bGo;
    private ImageButton bLocateMe;
    private ApiInterface mApiInterface;
    private ProgressBar pbProgress;
    private WeatherFragment weatherFragment;
    private String selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        mApiInterface = RetrofitClient.getInstance().create(ApiInterface.class);

        mPlaceAutocompleteFragment = (PlaceAutocompleteFragment) MainActivity.this.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mPlaceAutocompleteFragment.setFilter(new AutocompleteFilter.Builder().setCountry("US").setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build());
        mPlaceAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "onPlaceSelected: " + place.getName());
                selectedPlace = place;
                selectedCity = String.valueOf(selectedPlace.getName());
                sbLatLng.setLength(0);
                sbLatLng.append(selectedPlace.getLatLng().latitude).append(",").append(selectedPlace.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "onError: " + status);
            }
        });

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mGeocoder= new Geocoder(MainActivity.this, Locale.getDefault());

        pbProgress = findViewById(R.id.pb_progress);

        bLocateMe = findViewById(R.id.ibtn_locate_me);
        bLocateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                if (lastKnownLocation != null) {
                    getWeather();
                }
            }
        });

        bGo = findViewById(R.id.btn_go);
        bGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPlace != null) {
                    getWeather();
                } else if (lastKnownLocation != null) {
                    checkPermissions();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_select_location), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getWeather() {
        Call<WeatherResponse> call = mApiInterface.getWeather(sbLatLng.toString());
        pbProgress.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.i(TAG, "onResponse: Success!");
                pbProgress.setVisibility(View.GONE);
                if (mFragmentManager.findFragmentById(R.id.fl_content) instanceof WeatherFragment) {
                    weatherFragment.setWeatherResponse(response.body(), selectedCity);
                    weatherFragment.updateUI();
                } else {
                    weatherFragment = new WeatherFragment();
                    weatherFragment.setWeatherResponse(response.body(), selectedCity);
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fl_content, weatherFragment).commit();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                pbProgress.setVisibility(View.GONE);
            }
        });
    }

    private void checkPermissions() {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if ((ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSIONS_CODE);
        } else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            lastKnownLocation = location;
                            try {
                                selectedCity = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getLocality();
                            } catch (IOException ioe) {
                                Log.e(TAG, "onSuccess: ", ioe);
                            }
                            sbLatLng.setLength(0);
                            sbLatLng.append(location.getLatitude()).append(",").append(location.getLongitude());
                            getWeather();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissions();
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.toast_location_permission), Toast.LENGTH_SHORT).show();
        }
    }
}
