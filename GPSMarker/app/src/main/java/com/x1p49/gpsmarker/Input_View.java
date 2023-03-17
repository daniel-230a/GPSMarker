package com.x1p49.gpsmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Input_View extends AppCompatActivity {

    Button btn_save_loc;
    EditText name_val, description_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input__view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_save_loc = findViewById(R.id.btn_save_loc);
        name_val = findViewById(R.id.name_val);
        description_val = findViewById(R.id.description_val);

        final TextView longitude_val = (TextView) findViewById(R.id.longitude_val);
        final TextView latitude_val = (TextView) findViewById(R.id.latitude_val);
        final TextView altitude_val = (TextView) findViewById(R.id.altitude_val);

        Bundle save_location_values = getIntent().getExtras();


        final String save_longitude = save_location_values.getString("longitude");
        final String save_latitude = save_location_values.getString("latitude");
        final String save_altitude = save_location_values.getString("altitude");

        longitude_val.setText(save_longitude);
        latitude_val.setText(save_latitude);
        altitude_val.setText(save_altitude);

        btn_save_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String save_name = String.valueOf(name_val.getText());
                String save_description = String.valueOf(description_val.getText());

                if (save_name.isEmpty()) {
                    Toast.makeText(Input_View.this, "Location name is required", Toast.LENGTH_SHORT).show();
                } else {
                    DB_Handler db_handler = new DB_Handler(Input_View.this);
                    db_handler.insertLocation(save_name, save_description, save_longitude, save_latitude, save_altitude);

                    Toast.makeText(Input_View.this, "Added location:  "+  save_name, Toast.LENGTH_LONG).show();
                    openMainView();
                }


            }
        });

    }

    public void openMainView() {
        Intent main_view = new Intent(this, MainActivity.class);
        startActivity(main_view);
    }

}