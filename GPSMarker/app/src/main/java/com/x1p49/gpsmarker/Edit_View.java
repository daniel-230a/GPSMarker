package com.x1p49.gpsmarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Edit_View extends AppCompatActivity {

    Button btn_save_loc;
    EditText name_val, description_val;
    DB_Handler db_handler = new DB_Handler(Edit_View.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_save_loc = findViewById(R.id.btn_save_loc);
        name_val = findViewById(R.id.name_val);
        description_val = findViewById(R.id.description_val);


        final Bundle edit_location_values = getIntent().getExtras();

        btn_save_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String save_name = String.valueOf(name_val.getText());
                String save_description = String.valueOf(description_val.getText());

                if (save_name.isEmpty()) {
                    Toast.makeText(Edit_View.this, "Location name is required", Toast.LENGTH_SHORT).show();
                } else {
                    DB_Handler db_handler = new DB_Handler(Edit_View.this);
                    db_handler.updateLocation(edit_location_values.getString("id"), save_name, save_description);

                    openListView();
                    Toast.makeText(Edit_View.this, "Updated location:  "+ name_val.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        final EditText name_val = (EditText) findViewById(R.id.name_val);
        final EditText description_val = (EditText) findViewById(R.id.description_val);
        final TextView longitude_val = (TextView) findViewById(R.id.longitude_val);
        final TextView latitude_val = (TextView) findViewById(R.id.latitude_val);
        final TextView altitude_val = (TextView) findViewById(R.id.altitude_val);

        final String edit_name = edit_location_values.getString("name");
        final String edit_description = edit_location_values.getString("description");
        final String edit_longitude = edit_location_values.getString("longitude");
        final String edit_latitude = edit_location_values.getString("latitude");
        final String edit_altitude = edit_location_values.getString("altitude");

        name_val.setText(edit_name);
        description_val.setText(edit_description);
        longitude_val.setText(edit_longitude);
        latitude_val.setText(edit_latitude);
        altitude_val.setText(edit_altitude);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        final Bundle edit_location_values = getIntent().getExtras();

        switch (item.getItemId()) {
            case R.id.icon_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_View.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete " + edit_location_values.getString("name"));
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db_handler.deleteLocation(edit_location_values.getString("id"));
                                openListView();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void openListView() {
        Intent list_view = new Intent(this, List_View.class);
        startActivity(list_view);
    }
}