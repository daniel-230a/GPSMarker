package com.x1p49.gpsmarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class List_View extends AppCompatActivity {

    ListView list_view;
    DB_Handler db_handler = new DB_Handler(List_View.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Object[] list_view_items = db_handler.getListView();
        final ArrayList<String> list_item_id = (ArrayList<String>) list_view_items[0];
        ArrayList<String> list_item = (ArrayList<String>) list_view_items[1];

        list_view = findViewById(R.id.list_view);

        ArrayAdapter adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1,  list_item);

        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openEditView(Integer.parseInt(list_item_id.get((int) id)));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        final Bundle edit_location_values = getIntent().getExtras();

        switch (item.getItemId()) {
            case R.id.icon_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(List_View.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete all locations");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db_handler.deleteAllLocations();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    public void openEditView(int id) {
        Intent edit_view = new Intent(this, Edit_View.class);

        ArrayList<String> location = db_handler.getLocationByID(id);

        edit_view.putExtra("id", String.valueOf(location.get(0)));
        edit_view.putExtra("name", String.valueOf(location.get(1)));
        edit_view.putExtra("description",String.valueOf(location.get(2)));
        edit_view.putExtra("longitude", String.valueOf(location.get(3)));
        edit_view.putExtra("latitude",String.valueOf(location.get(4)));
        edit_view.putExtra("altitude", String.valueOf(location.get(5)));
        startActivity(edit_view);


    }


    public void openListView() {
        Intent list_view = new Intent(this, List_View.class);
        startActivity(list_view);
    }
}
