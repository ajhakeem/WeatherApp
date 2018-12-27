package projects.jaseem.weatherapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FragmentManager fragmentManager;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private Button bGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        placeAutocompleteFragment = (PlaceAutocompleteFragment) MainActivity.this.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setFilter(new AutocompleteFilter.Builder().setCountry("US").build());
        
    }
}
