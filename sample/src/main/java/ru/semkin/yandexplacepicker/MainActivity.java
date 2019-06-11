package ru.semkin.yandexplacepicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 1001;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenPlacePicker = findViewById(R.id.btnOpenPlacePicker);
        btnOpenPlacePicker.setOnClickListener(v -> showPlacePicker());
    }

    private void showPlacePicker() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
            return;
        }

        YandexPlacePicker.IntentBuilder builder = new YandexPlacePicker.IntentBuilder();
        builder.setYandexMapsKey("YOUR_MAPKIT_API_KEY"); // TODO Replace with your MapKit API key

        Intent placeIntent = builder.build(this);
        startActivityForResult(placeIntent, REQUEST_PLACE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_PLACE_PICKER)) {
            if(data != null) {
                PlaceParcelable place = YandexPlacePicker.getPlace(data);
                Toast.makeText(this, "You selected: " + place.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPlacePicker();
                }
            }
        }
    }
}
