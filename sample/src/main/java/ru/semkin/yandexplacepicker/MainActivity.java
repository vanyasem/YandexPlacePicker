package ru.semkin.yandexplacepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenPlacePicker = findViewById(R.id.btnOpenPlacePicker);
        btnOpenPlacePicker.setOnClickListener(v -> showPlacePicker());
    }

    private void showPlacePicker() {
        YandexPlacePicker.IntentBuilder builder = new YandexPlacePicker.IntentBuilder();
        builder.setYandexMapsKey("YOUR_MAPKIT_API_KEY"); // TODO Replace with your MapKit API key

        Intent placeIntent = builder.build(this);
        startActivityForResult(placeIntent, REQUEST_PLACE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_PLACE_PICKER)) {
            PlaceParcelable place = YandexPlacePicker.getPlace(data);
            if (place != null) {
                Toast.makeText(this, "You selected: " + place.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
