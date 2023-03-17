package com.x1p49.gpsmarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener = new MyLocationListener();  // The listener that responds to location updates.

    Button btn_add_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add_loc = findViewById(R.id.btn_add_loc);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        btn_add_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openInputView(); }
        });

        try {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);  // Requesting a location broadcast every 1000ms and a minimum distance between locations of 1m.

        } catch (SecurityException e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.icon_list_view:
                DB_Handler db_handler = new DB_Handler(MainActivity.this);
                Intent list_view = new Intent(this, List_View.class);
                startActivity(list_view);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void openInputView() {
        final TextView longitude_val = (TextView) findViewById(R.id.longitude_val);
        final TextView latitude_val = (TextView) findViewById(R.id.latitude_val);
        final TextView altitude_val = (TextView) findViewById(R.id.altitude_val);

        Intent input_view = new Intent(this, Input_View.class);
        input_view.putExtra("longitude", String.valueOf(longitude_val.getText()));
        input_view.putExtra("latitude",String.valueOf(latitude_val.getText()));
        input_view.putExtra("altitude", String.valueOf(altitude_val.getText()));

        if (longitude_val.getText().toString().equals("N/a")) {
            Toast.makeText(MainActivity.this, "Check if location permission is Enabled", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(input_view);
        }
    }

    // Inner class
    public class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {

            final TextView longitude_val = (TextView) findViewById(R.id.longitude_val);
            final TextView latitude_val = (TextView) findViewById(R.id.latitude_val);
            final TextView altitude_val = (TextView) findViewById(R.id.altitude_val);

            longitude_val.setText(String.valueOf(String.format("%.4f", location.getLongitude())));
            latitude_val.setText(String.valueOf(String.format("%.4f", location.getLatitude())));
            altitude_val.setText(String.valueOf(String.format("%.4f",location.getAltitude())));

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);  // Must stay here otherwise the GPS receiver will not stop working and will quickly consume the battery of the device.
    }
}